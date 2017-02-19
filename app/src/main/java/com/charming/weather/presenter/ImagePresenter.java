package com.charming.weather.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.charming.weather.comminterface.OnResponseCallback;
import com.charming.weather.util.HashKeyUtil;
import com.charming.weather.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by CharmingWong on 2016/12/8.
 */

public class ImagePresenter implements IPresenter {

    private static final String TAG = "image_data";

    private OnResponseCallback mOnResponseCallback;
    private String url;

    private  Context mContext;

    private static ImagePresenter instance;

    private ImagePresenter(Context context) {
        mContext = context;
    }

    //单例模式
    public static ImagePresenter getInstance(Context context) {
        if (instance == null) {
            synchronized (ImagePresenter.class) {
                if (instance == null) {
                    instance = new ImagePresenter(context);
                    return instance;
                }
            }
        }
        return instance;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //设置请求回调
    public void setOnResponseCallback(OnResponseCallback callback) {
        mOnResponseCallback = callback;
    }

    //开始主导
    @Override
    public void startPresent() {
        Bitmap bitmap = (Bitmap) loadDataFromLocal();
        if (bitmap != null) {
            Log.i(TAG, "startPresent: bitmap is null");
            returnData(bitmap);
        } else {
            if (NetworkUtil.checkNetworkStatus(mContext)) {
                Log.i(TAG, "startPresent: load data from network");
                requestData();
            }
        }
    }

    //请求数据
    private void requestData() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        Response.Listener listener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                onRequestCompleted(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(mContext, mContext.getString(R.string.host_error_text), Toast.LENGTH_SHORT).show();
            }
        };

        ImageRequest imageRequest =
                new ImageRequest(
                        url,
                        listener,
                        480,
                        360,
                        ImageView.ScaleType.FIT_XY,
                        Bitmap.Config.ARGB_8888,
                        errorListener);

        requestQueue.add(imageRequest);
        requestQueue.start();
    }

    //请求完成
    @Override
    public void onRequestCompleted(Object result) {
        parseData(result);
        writeDataToLocal(result);
    }

    //解析数据
    private void parseData(Object data) {
        returnData(data);
    }

    //返回数据
    @Override
    public void returnData(Object result) {
        if (mOnResponseCallback != null) {
            if (result != null) {
                if (result instanceof Bitmap) {
                    mOnResponseCallback.onResponseSuccess(result);
                } else {
                    mOnResponseCallback.onResponseError(((Exception) result));
                }
            } else {
                mOnResponseCallback.onResponseError(new Exception("request error null"));
            }
        }
    }

    //写入缓存
    private void writeDataToLocal(Object parsedData) {
        FileOutputStream fos = null;
        ByteArrayOutputStream baos;
        try {
            if (parsedData != null) {
                Bitmap bitmap = (Bitmap) parsedData;
                baos = new ByteArrayOutputStream();
                boolean isCompressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                if (isCompressed) {
                    String filename = HashKeyUtil.toMD5(baos.toByteArray());
                    if (filename != null && filename.equals("")) {
                        File file = new File(mContext.getExternalCacheDir(), filename);
                        fos = new FileOutputStream(file);
                        fos.write(baos.toByteArray());
                        HashKeyUtil.toMD5(baos.toByteArray());
                        Log.i(TAG, "writeDataToLocal: wrote data successful");
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "writeDataToLocal: failed to write data");
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //读取缓存
    private Object loadDataFromLocal() {
        try {
            Log.i(TAG, "loadDataFromLocal: " + mContext.getExternalCacheDir() + "/" + url.substring(url.lastIndexOf("/") + 1, url.length()));
            return BitmapFactory.decodeFile(mContext.getExternalCacheDir() + "/" + url.substring(url.lastIndexOf("/") + 1, url.length()));
        } catch (Exception exception) {
            //本地加载数据失败，从网络获取数据
            Log.i(TAG, "loadDataFromLocal: failed to load data from local");
            Log.i(TAG, "loadDataFromLocal: load data from network");
            exception.printStackTrace();
            return null;
        }
    }

}

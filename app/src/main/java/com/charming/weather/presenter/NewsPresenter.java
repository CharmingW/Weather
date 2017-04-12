package com.charming.weather.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.charming.weather.R;
import com.charming.weather.comminterface.OnResponseCallback;
import com.charming.weather.parser.NewsGsonParser;
import com.charming.weather.util.ApplicationUtil;
import com.charming.weather.util.HashKeyUtil;
import com.charming.weather.util.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CharmingWong on 2016/12/8.
 */

public class NewsPresenter implements IPresenter {
    private static final String TAG = "NewsPresenter";
    private final String DATA_TAG = "news_data";
    private String mUrl;
    private Context mContext;
    private NewsGsonParser mDataParser;
    private OnResponseCallback mOnResponseCallback;

    public void setChannel(String channel) {
        mUrl = "http://v.juhe.cn/toutiao/index?key=6b88d59b03a95861f8b2a9938738d625&type=" + channel;
    }

    public NewsPresenter(Context context) {
        mContext = context;
        mDataParser = new NewsGsonParser();
    }

    //设置请求回调
    public void setOnResponseCallback(OnResponseCallback callback) {
        mOnResponseCallback = callback;
    }

    //开始主导
    @Override
    public void startPresent() {
        boolean isNetworkAvailable = NetworkUtil.checkNetworkStatus(mContext);
        boolean isUpdateAvailable = checkDataUpdate();
        if (isNetworkAvailable && isUpdateAvailable) {
            Log.i(TAG, "startPresent: load data from network");
            requestData();
        } else {
            Log.i(TAG, "startPresent: load data from local");
            Object data = loadWeatherDataFromLocal();
            if (data != null) {
                returnData(data);
            } else {
                requestData();
            }
        }
    }

    //请求数据e
    public void requestData() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());

        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                onRequestCompleted(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, mContext.getString(R.string.host_error_text), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
                returnData(null);
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl, listener, errorListener);
        Log.i("NewsPresenter", "requestData: " + stringRequest.getUrl());
        requestQueue.add(stringRequest);
        requestQueue.start();
    }

    //请求完成
    @Override
    public void onRequestCompleted(Object result) {
        parseData(result);
    }

    //解析数据
    public void parseData(Object data) {
        Map<String, Object> map = new HashMap<>();
        if (mDataParser != null) {
            map = mDataParser.parse((String) data);
        }
        returnData(map);
        writeWeatherDataToLocal(map);
    }

    //返回数据
    @Override
    public void returnData(Object result) {
        if (mOnResponseCallback != null) {
            if (result != null) {
                if (result instanceof Map) {
                    mOnResponseCallback.onResponseSuccess(result);
                } else {
                    mOnResponseCallback.onResponseError(((Exception) result));
                }
            } else {
                Log.i(TAG, "returnData: " + "null");
                mOnResponseCallback.onResponseError(new Exception("request error null"));
            }
        }
    }

    //写入缓存
    private void writeWeatherDataToLocal(Object parsedData) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            if (parsedData != null) {
                File file = new File(
                        ApplicationUtil.getDiskCacheDir(mContext, DATA_TAG),
                        HashKeyUtil.generateHashKey(mUrl)
                );
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(parsedData);
                oos.flush();

                //更新时间
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(DATA_TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(mUrl, System.currentTimeMillis());
                editor.apply();
                Log.i(TAG, "writeNewsDataToLocal: wrote weather data successfully");
            }
        } catch (Exception e) {
            Log.i(TAG, "writeNewsDataToLocal: failed to write weather data");
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //读取缓存
    public Object loadWeatherDataFromLocal() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File file = new File(
                    ApplicationUtil.getDiskCacheDir(mContext, DATA_TAG),
                    HashKeyUtil.generateHashKey(mUrl)
            );
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Log.i(TAG, "loadNewsDataFromLocal: load data from local successful");
            return ois.readObject();
        } catch (Exception exception) {
            //本地加载数据失败，从网络获取数据
            Log.i(TAG, "loadNewsDataFromLocal: failed to load data from local");
            Log.i(TAG, "loadDataFromLocal: load data from network");
            exception.printStackTrace();
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //检查更新
    public boolean checkDataUpdate() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DATA_TAG, Context.MODE_PRIVATE);
        long lastUpdate = sharedPreferences.getLong(mUrl, 0);
        //距离上次更新过去15分钟则再次更新
        if (System.currentTimeMillis() - lastUpdate > 30 * 1000) {
            return true;
        }
        return false;
    }
}

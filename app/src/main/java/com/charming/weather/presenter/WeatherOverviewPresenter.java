package com.charming.weather.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.charming.weather.R;
import com.charming.weather.comminterface.OnResponseCallback;
import com.charming.weather.parser.DataParser;
import com.charming.weather.parser.GlobalGsonParser;
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
 * Created by CharmingWong on 2016/11/28.
 */

public class WeatherOverviewPresenter implements IPresenter {

    private String httpUrl;
    private final String DATA_TAG = "weather_data";
    private static final String TAG = "WeatherPresenter";

    private OnResponseCallback mOnResponseCallback;
    private DataParser mDataParser;

    private Context mContext;

    public WeatherOverviewPresenter(Context context) {
        mContext = context;
        mDataParser = new GlobalGsonParser();
    }

    //设置请求回调
    public void setOnResponseCallback(OnResponseCallback callback) {
        mOnResponseCallback = callback;
    }

    //开始主导
    @Override
    public void startPresent() {
        SharedPreferences spf = mContext.getSharedPreferences("location_city", Context.MODE_PRIVATE);
        String city = spf.getString("city", null);
        httpUrl = "http://apis.baidu.com/heweather/pro/weather?city=" + city;
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

    //请求数据
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

        Log.i(TAG, "requestData: " + httpUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, httpUrl, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("apikey", "c58b256f3b2c3b79dad4320888b8e5e3");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
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
            map = mDataParser.parse(data);
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
                    Toast.makeText(mContext, R.string.refresh_success, Toast.LENGTH_SHORT).show();
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
        if (parsedData != null) {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                File file = new File(
                        ApplicationUtil.getDiskCacheDir(mContext, "weather"),
                        HashKeyUtil.generateHashKey(httpUrl)
                );
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(parsedData);
                oos.flush();

                //更新时间
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(DATA_TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(httpUrl, System.currentTimeMillis());
                editor.apply();
                Log.i(TAG, "writeWeatherDataToLocal: wrote weather data successful");
            } catch (Exception e) {
                Log.i(TAG, "writeWeatherDataToLocal: failed to write weather data");
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
    }

    //读取缓存
    public Object loadWeatherDataFromLocal() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File file = new File(
                    ApplicationUtil.getDiskCacheDir(mContext, "weather"),
                    HashKeyUtil.generateHashKey(httpUrl)
            );
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Log.i(TAG, "loadDataFromLocal: load data from local successful");
            return ois.readObject();
        } catch (Exception exception) {
            //本地加载数据失败，从网络获取数据
            Log.i(TAG, "loadDataFromLocal: failed to load data from local");
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
        long lastUpdate = sharedPreferences.getLong(httpUrl, 0);
        //距离上次更新过去60分钟则再次更新
        if (System.currentTimeMillis() - lastUpdate > 60 * 60 * 1000) {
            return true;
        }
        return false;
    }


}

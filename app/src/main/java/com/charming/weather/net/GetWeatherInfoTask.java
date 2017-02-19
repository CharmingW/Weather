package com.charming.weather.net;

import android.os.AsyncTask;
import android.util.Log;

import com.charming.weather.comminterface.OnResponseCallback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 56223 on 2016/9/22.
 */

public class GetWeatherInfoTask extends AsyncTask<String, Integer, Object> {

    private static final String TAG = "GetNewsTask";
    private static final String API_KEY = "c58b256f3b2c3b79dad4320888b8e5e3";
    String httpUrl = "http://apis.baidu.com/thinkpage/weather_api/suggestion";
    String httpArg = "location=guangzhou&language=zh-Hans&unit=c&start=0&days=3";

    private OnResponseCallback responseCallback;


    public void setResponseCallback(OnResponseCallback callback) {
        responseCallback = callback;
    }

    /**
     * @param
     *            :请求接口
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  API_KEY);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected Object doInBackground(String... params) {
       return request(httpUrl, httpArg);
    }

    @Override
    protected void onPostExecute(Object result) {
        if (responseCallback != null) {
            if (result instanceof String) {
                Log.d(TAG, "onResponseSuccess");
                Log.d(TAG, ((String) result));
                responseCallback.onResponseSuccess((String) result);
            } else if (result instanceof Exception) {
                Log.d(TAG, "onResponseError");
                responseCallback.onResponseError((Exception) result);
            } else {
                Log.e(TAG, "Unknown type data");
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }
}

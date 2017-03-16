package com.charming.weather.parser;

import android.util.Log;

import com.charming.weather.entity.weather.HeWeatherDataService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class GlobalGsonParser implements GsonParser {

    private static final String TAG = "GlobalGsonParser";

    @Override
    public Map<String, Object> parse(String data) {
        data = adjustString(data);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeatherDataService");
            Gson gson = new Gson();
            HeWeatherDataService heWeatherDataService = gson.fromJson(jsonArray.get(0).toString(), HeWeatherDataService.class);
            Log.i(TAG, "parse: " + jsonArray.get(0));
            if ("ok".equals(heWeatherDataService.getStatus())) {
                Map<String, Object> map = new HashMap<>();
                map.put("aqi", heWeatherDataService.getAqi());
                map.put("basic", heWeatherDataService.getBasic());
                map.put("daily_forecast", heWeatherDataService.getDaily_forecast());
                map.put("hourly_forecast", heWeatherDataService.getHourly_forecast());
                map.put("now", heWeatherDataService.getNow());
                map.put("suggestion", heWeatherDataService.getSuggestion());
                return map;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String adjustString(String oldString) {
        return oldString.replace("HeWeather data service 3.0", "HeWeatherDataService");
    }
}

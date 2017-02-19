package com.charming.weather.parser;

import java.util.Map;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class WeatherGsonParser implements GsonParser {

    @Override
    public Map<String, Object> parse(String data) {
//        Gson gson = new Gson();
//        try {
//            Root root = gson.fromJson(data, Root.class);
////            List<Results> results = root.getResults();
//            Results result1 = results.get(0);
//            Location location = result1.getLocation();
//            List<Daily> dailyArrayList = result1.getDaily();
//            Map<String, Object> resultMap = new HashMap<>();
//            resultMap.put("location", location);
//            resultMap.put("daily", dailyArrayList);
//            return resultMap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }
}

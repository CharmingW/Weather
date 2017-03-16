package com.charming.weather.parser;

import com.charming.weather.entity.news.Root;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CharmingWong on 2016/12/8.
 */

public class NewsGsonParser implements GsonParser {

    public Map<String, Object> parse(String data) {
        Gson gson = new Gson();
        try {
            Root root = gson.fromJson(data, Root.class);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("result", root.getResult());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

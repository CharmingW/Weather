package com.charming.weather.parser;

import com.charming.weather.entity_news.Root;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CharmingWong on 2016/12/8.
 */

public class NewsGsonParser implements GsonParser {

    @Override
    public Map<String, Object> parse(String data) {
        Gson gson = new Gson();
        try {
            Root root = gson.fromJson(data, Root.class);
            if (root.getStatus()) {
                Map<String, Object> map = new HashMap<>();
                map.put("tngou", root.getTngou());
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

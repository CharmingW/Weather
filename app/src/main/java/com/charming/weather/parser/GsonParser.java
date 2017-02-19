package com.charming.weather.parser;

import java.util.Map;

/**
 * Created by 56223 on 2016/11/27.
 */

public interface GsonParser extends DataParser<String> {

    public Map<String, Object> parse(String data);
}

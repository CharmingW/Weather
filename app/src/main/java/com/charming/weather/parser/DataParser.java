package com.charming.weather.parser;

/**
 * Created by 56223 on 2016/11/28.
 */

import java.util.Map;

/**
 * 用于解析请求网络返回数据的接口
 **/
public interface DataParser<T> {

    /**
     * 解析网络返回的数据
     *
     * @param data 网络返回的数据参数，用泛型表示，可以为String，JSONObject，Bitmap等等
     * @return Map 封装解析之后的数据
     */
    public Map<String, Object> parse(T data);
}

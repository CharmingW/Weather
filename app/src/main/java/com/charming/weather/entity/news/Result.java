/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.charming.weather.entity.news;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    private List<Data> data;
    private String stat;

    public void setStat(String stat) {
        stat = stat;
    }

    public String getStat() {
        return stat;
    }

    public void setData(List<Data> data) {
        data = data;
    }

    public List getData() {
        return data;
    }
}

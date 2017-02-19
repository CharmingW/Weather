package com.charming.weather.entity;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Tmp implements Serializable {
    private String max;

    private String min;

    public void setMax(String max){
        this.max = max;

        String s = new String();
    }
    public String getMax(){
        return this.max;
    }
    public void setMin(String min){
        this.min = min;
    }
    public String getMin(){
        return this.min;
    }

}

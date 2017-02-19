package com.charming.weather.entity;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Comf implements Serializable {
    private String brf;

    private String txt;

    public void setBrf(String brf){
        this.brf = brf;
    }
    public String getBrf(){
        return this.brf;
    }
    public void setTxt(String txt){
        this.txt = txt;
    }
    public String getTxt(){
        return this.txt;
    }

}

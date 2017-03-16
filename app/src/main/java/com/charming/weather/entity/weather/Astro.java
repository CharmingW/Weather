package com.charming.weather.entity.weather;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Astro implements Serializable {
    private String sr;

    private String ss;

    public void setSr(String sr){
        this.sr = sr;
    }
    public String getSr(){
        return this.sr;
    }
    public void setSs(String ss){
        this.ss = ss;
    }
    public String getSs(){
        return this.ss;
    }

}

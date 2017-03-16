package com.charming.weather.entity.weather;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Update implements Serializable {
    private String loc;

    private String utc;

    public void setLoc(String loc){
        this.loc = loc;
    }
    public String getLoc(){
        return this.loc;
    }
    public void setUtc(String utc){
        this.utc = utc;
    }
    public String getUtc(){
        return this.utc;
    }

}
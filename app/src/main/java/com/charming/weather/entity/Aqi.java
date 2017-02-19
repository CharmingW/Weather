package com.charming.weather.entity;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Aqi implements Serializable {
    private City city;

    public void setCity(City city){
        this.city = city;
    }
    public City getCity(){
        return this.city;
    }

}
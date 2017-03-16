package com.charming.weather.entity.weather;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Root implements Serializable {
    private List<HeWeatherDataService> heWeatherDataService;

    public void setHeWeatherDataService(List<HeWeatherDataService> heWeatherDataService){
        this.heWeatherDataService = heWeatherDataService;
    }
    public List<HeWeatherDataService> getHeWeatherDataService(){
        return this.heWeatherDataService;
    }

}

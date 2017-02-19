package com.charming.weather.presenter;

/**
 * Created by CharmingWong on 2016/11/28.
 */

public interface IPresenter {

    public void startPresent();

    public void onRequestCompleted(Object result);

    public void returnData(Object ParsedData) throws Exception;

}
package com.charming.weather.entity_news;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CharmingWong on 2016/12/8.
 */

public class Root implements Serializable {
    private boolean status;

    private int total;

    private List<Tngou> tngou;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTngou(List<Tngou> tngou) {
        this.tngou = tngou;
    }

    public List<Tngou> getTngou() {
        return this.tngou;
    }

}
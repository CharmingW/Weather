package com.charming.weather.entity_news;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/8.
 */

public class Tngou implements Serializable {
    private int count;

    private String description;

    private int fcount;

    private int id;

    private String img;

    private int infoclass;

    private String keywords;

    private int rcount;

    private long time;

    private String title;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getFcount() {
        return this.fcount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return this.img;
    }

    public void setInfoclass(int infoclass) {
        this.infoclass = infoclass;
    }

    public int getInfoclass() {
        return this.infoclass;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public int getRcount() {
        return this.rcount;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}

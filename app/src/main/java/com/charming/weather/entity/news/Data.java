/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.charming.weather.entity.news;

import java.io.Serializable;

public class Data implements Serializable {
    private String author_name;
    private String category;
    private String date;
    private String thumbnail_pic_s;
    private String thumbnail_pic_s02;
    private String thumbnail_pic_s03;
    private String title;
    private String uniquekey;
    private String url;

    public void setUniquekey(String uniquekey) {
        uniquekey = uniquekey;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(String date) {
        date = date;
    }

    public String getDate() {
        return date;
    }

    public void setCategory(String category) {
        category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setAuthor_name(String author_name) {
        author_name = author_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setUrl(String url) {
        url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        thumbnail_pic_s02 = thumbnail_pic_s02;
    }

    public String getThumbnail_pic_s02() {
        return thumbnail_pic_s02;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }
}

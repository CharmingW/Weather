package com.charming.weather.entity.weather;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Cond implements Serializable {
    private String code;

    private String txt;

    private String code_d;

    private String code_n;

    private String txt_d;

    private String txt_n;

    public void setCode_d(String code_d) {
        this.code_d = code_d;
    }

    public String getCode_d() {
        return this.code_d;
    }

    public void setCode_n(String code_n) {
        this.code_n = code_n;
    }

    public String getCode_n() {
        return this.code_n;
    }

    public void setTxt_d(String txt_d) {
        this.txt_d = txt_d;
    }

    public String getTxt_d() {
        return this.txt_d;
    }

    public void setTxt_n(String txt_n) {
        this.txt_n = txt_n;
    }

    public String getTxt_n() {
        return this.txt_n;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getTxt() {
        return this.txt;
    }

}
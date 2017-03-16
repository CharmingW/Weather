package com.charming.weather.entity.weather;

import java.io.Serializable;

/**
 * Created by CharmingWong on 2016/12/3.
 */

public class Suggestion implements Serializable {
    private Air air;

    private Comf comf;

    private Cw cw;

    private Drsg drsg;

    private Flu flu;

    private Sport sport;

    private Trav trav;

    private Uv uv;

    public void setAir(Air air){
        this.air = air;
    }
    public Air getAir(){
        return this.air;
    }
    public void setComf(Comf comf){
        this.comf = comf;
    }
    public Comf getComf(){
        return this.comf;
    }
    public void setCw(Cw cw){
        this.cw = cw;
    }
    public Cw getCw(){
        return this.cw;
    }
    public void setDrsg(Drsg drsg){
        this.drsg = drsg;
    }
    public Drsg getDrsg(){
        return this.drsg;
    }
    public void setFlu(Flu flu){
        this.flu = flu;
    }
    public Flu getFlu(){
        return this.flu;
    }
    public void setSport(Sport sport){
        this.sport = sport;
    }
    public Sport getSport(){
        return this.sport;
    }
    public void setTrav(Trav trav){
        this.trav = trav;
    }
    public Trav getTrav(){
        return this.trav;
    }
    public void setUv(Uv uv){
        this.uv = uv;
    }
    public Uv getUv(){
        return this.uv;
    }

}

package com.github.dcf82.beans;

import com.google.gson.annotations.SerializedName;

/**
 * @author David Castillo Fuentes
 */
public class BaseWeatherData {

    @SerializedName("weather")
    protected Weather[] mWeathers;

    @SerializedName("main")
    protected Main mMain;

    @SerializedName("wind")
    protected Wind mWind;

    @SerializedName("rain")
    protected Rain mRain;

    @SerializedName("cod")
    protected int mCode;

    @SerializedName("name")
    protected String mName;

    public BaseWeatherData() {
    }

    public Weather[] getWeather() {
        return mWeathers;
    }

    public void setWeather(Weather[] weathers) {
        this.mWeathers = weathers;
    }

    public Main getMain() {
        return mMain;
    }

    public void setMain(Main main) {
        this.mMain = main;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind wind) {
        this.mWind = wind;
    }

    public Rain getRain() {
        return mRain;
    }

    public void setRain(Rain rain) {
        this.mRain = rain;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

}

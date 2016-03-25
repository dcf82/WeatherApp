package com.github.dcf82.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author David Castillo Fuentes
 */
public class ForestRequest implements Parcelable {

    @SerializedName("city")
    private City mCity;

    @SerializedName("list")
    private WeatherDay[] mWeatherDays;

    @SerializedName("cod")
    private int mCode;

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        this.mCity = city;
    }

    public WeatherDay[] getWeatherDays() {
        return mWeatherDays;
    }

    public void setWeatherDays(WeatherDay[] weatherDays) {
        this.mWeatherDays = weatherDays;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    private ForestRequest(Parcel in) {
        this.mCity = in.readParcelable(City.class.getClassLoader());
        this.mWeatherDays = (WeatherDay[])in.readArray(WeatherDay.class.getClassLoader());
        this.mCode = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mCity, flags);
        dest.writeArray(this.mWeatherDays);
        dest.writeInt(this.mCode);
    }

    public static final Parcelable.Creator<ForestRequest> CREATOR = new Parcelable.Creator<ForestRequest>() {

        @Override
        public ForestRequest createFromParcel(Parcel source) {
            return new ForestRequest(source);
        }

        @Override
        public ForestRequest[] newArray(int size) {
            return new ForestRequest[size];
        }
    };

}

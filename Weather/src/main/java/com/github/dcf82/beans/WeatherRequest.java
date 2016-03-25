package com.github.dcf82.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author David Castillo Fuentes
 */
public class WeatherRequest extends BaseWeatherData implements Parcelable {

    private WeatherRequest(Parcel in) {
        this.mWeathers = (Weather[])in.readArray(Weather.class.getClassLoader());
        this.mMain = in.readParcelable(Main.class.getClassLoader());
        this.mWind = in.readParcelable(Wind.class.getClassLoader());
        this.mRain = in.readParcelable(Rain.class.getClassLoader());
        this.mCode = in.readInt();
        this.mName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(this.mWeathers);
        dest.writeParcelable(this.mMain, flags);
        dest.writeParcelable(this.mWind, flags);
        dest.writeParcelable(this.mRain, flags);
        dest.writeInt(this.mCode);
        dest.writeString(this.mName);
    }

    public static final Parcelable.Creator<WeatherRequest> CREATOR = new Parcelable.Creator<WeatherRequest>() {

        @Override
        public WeatherRequest createFromParcel(Parcel source) {
            return new WeatherRequest(source);
        }

        @Override
        public WeatherRequest[] newArray(int size) {
            return new WeatherRequest[size];
        }
    };

}

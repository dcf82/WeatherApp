package com.github.dcf82.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author David Castillo Fuentes
 */
public class WeatherDay extends BaseWeatherData implements Parcelable {

    @SerializedName("dt")
    private long mDt;

    @SerializedName("dt_txt")
    private String mDtTxt;


    public long getDt() {
        return mDt;
    }

    public void setDt(long dt) {
        this.mDt = dt;
    }

    public String getDtTxt() {
        return mDtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.mDtTxt = dtTxt;
    }

    private WeatherDay(Parcel in) {
        this.mWeathers = (Weather[])in.readArray(Weather.class.getClassLoader());
        this.mMain = in.readParcelable(Main.class.getClassLoader());
        this.mWind = in.readParcelable(Wind.class.getClassLoader());
        this.mRain = in.readParcelable(Rain.class.getClassLoader());
        this.mCode = in.readInt();
        this.mName = in.readString();
        this.mDt = in.readLong();
        this.mDtTxt = in.readString();
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
        dest.writeLong(this.mDt);
        dest.writeString(this.mDtTxt);
    }

    public static final Parcelable.Creator<WeatherDay> CREATOR = new Parcelable.Creator<WeatherDay>() {

        @Override
        public WeatherDay createFromParcel(Parcel source) {
            return new WeatherDay(source);
        }

        @Override
        public WeatherDay[] newArray(int size) {
            return new WeatherDay[size];
        }
    };

}

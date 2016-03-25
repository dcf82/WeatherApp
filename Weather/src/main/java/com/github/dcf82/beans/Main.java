package com.github.dcf82.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author David Castillo Fuentes
 */
public class Main implements Parcelable {

    @SerializedName("temp")
    private double mTemp;

    @SerializedName("pressure")
    private double mPressure;

    @SerializedName("humidity")
    private double mHumidity;

    @SerializedName("temp_min")
    private double mTempMin;

    @SerializedName("temp_max")
    private double mTempMax;

    @SerializedName("sea_level")
    private double mSeaLevel;

    @SerializedName("grnd_level")
    private double mGrndLevel;

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(double temp) {
        this.mTemp = temp;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(double pressure) {
        this.mPressure = pressure;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        this.mHumidity = humidity;
    }

    public double getTempMin() {
        return mTempMin;
    }

    public void setTempMin(double tempMin) {
        this.mTempMin = tempMin;
    }

    public double getTempMax() {
        return mTempMax;
    }

    public void setTempMax(double tempMax) {
        this.mTempMax = tempMax;
    }

    public double getSeaLevel() {
        return mSeaLevel;
    }

    public void setSeaLevel(double seaLevel) {
        this.mSeaLevel = seaLevel;
    }

    public double getGrndLevel() {
        return mGrndLevel;
    }

    public void setGrndLevel(double grndLevel) {
        this.mGrndLevel = grndLevel;
    }

    private Main(Parcel in) {
        this.mTemp = in.readDouble();
        this.mPressure = in.readDouble();
        this.mHumidity = in.readDouble();
        this.mTempMin = in.readDouble();
        this.mTempMax = in.readDouble();
        this.mSeaLevel = in.readDouble();
        this.mGrndLevel = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mTemp);
        dest.writeDouble(this.mPressure);
        dest.writeDouble(this.mHumidity);
        dest.writeDouble(this.mTempMin);
        dest.writeDouble(this.mTempMax);
        dest.writeDouble(this.mSeaLevel);
        dest.writeDouble(this.mGrndLevel);
    }

    public static final Parcelable.Creator<Main> CREATOR = new Parcelable.Creator<Main>() {

        @Override
        public Main createFromParcel(Parcel source) {
            return new Main(source);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };

}

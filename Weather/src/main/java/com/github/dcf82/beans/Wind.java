package com.github.dcf82.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author David Castillo Fuentes
 */
public class Wind implements Parcelable {

    @SerializedName("speed")
    private double mSpeed;

    @SerializedName("deg")
    private double mDeg;

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public double getDeg() {
        return mDeg;
    }

    public void setDeg(double deg) {
        this.mDeg = deg;
    }

    private Wind(Parcel in) {
        this.mSpeed = in.readDouble();
        this.mDeg = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mSpeed);
        dest.writeDouble(this.mDeg);
    }

    public static final Parcelable.Creator<Wind> CREATOR = new Parcelable.Creator<Wind>() {

        @Override
        public Wind createFromParcel(Parcel source) {
            return new Wind(source);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };

}

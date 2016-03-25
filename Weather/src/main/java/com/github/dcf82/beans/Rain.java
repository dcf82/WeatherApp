package com.github.dcf82.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author David Castillo Fuentes
 */
public class Rain implements Parcelable {

    @SerializedName("3h")
    private double m3h;

    public double getM3h() {
        return m3h;
    }

    public void setM3h(double m3h) {
        this.m3h = m3h;
    }

    private Rain(Parcel in) {
        this.m3h = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.m3h);
    }

    public static final Parcelable.Creator<Rain> CREATOR = new Parcelable.Creator<Rain>() {

        @Override
        public Rain createFromParcel(Parcel source) {
            return new Rain(source);
        }

        @Override
        public Rain[] newArray(int size) {
            return new Rain[size];
        }
    };

}

package com.github.dcf82.interfaces;

import com.github.dcf82.beans.ForestRequest;
import com.github.dcf82.beans.WeatherRequest;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author David Castillo Fuentes
 * Interface file used by Retrofit to handle Restful requests for Weather & Forecast data
 */
public interface WeatherInterface {

    /**
     * Method to get the current weather information
     * @param latitude The latitude of user's location
     * @param longitude The longitude of user's location
     */
    @GET("weather?appid=ec686b53cdeb45bfcc6a48ae8ae2b7ec")
    Call<WeatherRequest> getWeatherInfo(@Query("lat") double latitude, @Query("lon") double
            longitude, @Query("units") String units);

    /**
     * Method to get the current forecast info for the next week, including today
     * @param latitude The latitude of user's location
     * @param longitude The longitude of user's location
     */
    @GET("forecast?appid=ec686b53cdeb45bfcc6a48ae8ae2b7ec")
    Call<ForestRequest> getForecastInfo(@Query("lat") double latitude, @Query("lon") double
            longitude, @Query("units") String units);

}

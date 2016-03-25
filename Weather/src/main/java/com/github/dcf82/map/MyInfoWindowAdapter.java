package com.github.dcf82.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.github.dcf82.R;
import com.github.dcf82.beans.WeatherRequest;
import com.github.dcf82.helpers.Utility;

/**
 * @author David Castillo Fuentes
 * Popup message that shows Weather information on a Google MapView
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private WeatherRequest mWeatherRequest;
    private final View mWindow;
    private Marker mMarker;

    public MyInfoWindowAdapter(Context context, WeatherRequest weatherRequest) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        mWeatherRequest = weatherRequest;
        mWindow = inflater.inflate(R.layout.my_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return buildView(marker);
    }

    private View buildView(Marker marker) {
        this.mMarker = marker;
        this.updateData();
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void updateData() {
        // Nothing to update
        if (mWeatherRequest == null) return;

        TextView tv;

        // Set weather description
        tv = (TextView)mWindow.findViewById(R.id.description);
        if (mWeatherRequest.getWeather() != null && mWeatherRequest.getWeather().length > 0) {
                Utility.setData(tv, "Weather: ", Utility.capitalize(mWeatherRequest.getWeather()
                        [0].getDescription(), " "));
        }

        // Set the temperature
        tv = (TextView)mWindow.findViewById(R.id.temperature);
        if (mWeatherRequest.getMain() != null) {
            Utility.setData(tv, "Curr. Temp.(ºC): ", mWeatherRequest.getMain().getTemp());
        }

        // Set the Wind Speed
        tv = (TextView)mWindow.findViewById(R.id.wind_speed);
        if (mWeatherRequest.getWind() != null) {
            Utility.setData(tv, "Wind Speed(m/s) : ", mWeatherRequest.getWind().getSpeed());
        }

        // Set the humidity
        tv = (TextView)mWindow.findViewById(R.id.humidity);
        if (mWeatherRequest.getMain() != null) {
            Utility.setData(tv, "Curr. Humidity(%): ", mWeatherRequest.getMain().getHumidity());
        }

        // Set the Min Temperature
        tv = (TextView)mWindow.findViewById(R.id.temp_min);
        if (mWeatherRequest.getMain() != null) {
            Utility.setData(tv, "Min. Temp.(ºC): ", mWeatherRequest.getMain().getTempMin());
        }

        // Set the Max Temperature
        tv = (TextView)mWindow.findViewById(R.id.temp_max);
        if (mWeatherRequest.getMain() != null) {
            Utility.setData(tv, "Max. Temp.(ºC): ", mWeatherRequest.getMain().getTempMax());
        }

        // Close progress bar
        openBar(false);
    }

    public void openBar(boolean open) {
        View bar = mWindow.findViewById(R.id.bar);
        if (open) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar .setVisibility(View.GONE);
        }
    }

    public void setWeatherData(WeatherRequest weatherRequest) {
        this.mWeatherRequest = weatherRequest;
        updateData();
    }

}

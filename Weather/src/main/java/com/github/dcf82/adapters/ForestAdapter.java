package com.github.dcf82.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dcf82.R;
import com.github.dcf82.beans.WeatherDay;
import com.github.dcf82.helpers.Utility;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author David Castillo Fuentes
 * Adapter used in a RecyclerView to show forecast data
 */
public class ForestAdapter
        extends RecyclerView.Adapter<ForestAdapter.ViewHolder> {

    private List<WeatherDay> mItems;
    private Calendar mCalendar = Calendar.getInstance();
    private SimpleDateFormat dh = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public ForestAdapter(List<WeatherDay> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item, parent, false));
    }

    public void setItems(WeatherDay[] items) {
        this.mItems.clear();
        this.mItems.addAll(Arrays.asList(items));
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Current Data
        WeatherDay item = mItems.get(position);

        // Current Time
        mCalendar.setTimeInMillis(1000L * item.getDt());

        // Set Date & Time
        holder.mDate.setText(dh.format(mCalendar.getTime()));

        // Description
        Utility.setData(holder.mDescription, "", Utility.capitalize(item.getWeather()[0]
                .getDescription(), " "));

        // Set the temperature
        Utility.setData(holder.mTemperature, "Curr. Temp.(ºC): ", item.getMain().getTemp());

        // Set the Humidity
        Utility.setData(holder.mHumidity, "Humidity(%): ", item.getMain().getHumidity());

        // Set the Wind Speed
        Utility.setData(holder.mWindSpeed, "Wind Speed(m/s): ", item.getWind().getSpeed());

        // Set the Min Temp
        Utility.setData(holder.mTempMin, "Min. Temp.(ºC): ", item.getMain().getTempMin());

        // Set the Max Temp
        Utility.setData(holder.mTempMax, "Max. Temp.(ºC): ", item.getMain().getTempMax());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mDate;
        TextView mDescription;
        TextView mTemperature;
        TextView mHumidity;
        TextView mWindSpeed;
        TextView mTempMin;
        TextView mTempMax;

        public ViewHolder(View view) {
            super(view);
            mDate = (TextView)view.findViewById(R.id.date);
            mDescription = (TextView)view.findViewById(R.id.description);
            mTemperature = (TextView)view.findViewById(R.id.temperature);
            mHumidity = (TextView)view.findViewById(R.id.humidity);
            mWindSpeed = (TextView)view.findViewById(R.id.wind_speed);
            mTempMin = (TextView)view.findViewById(R.id.temp_min);
            mTempMax = (TextView)view.findViewById(R.id.temp_max);
        }

    }

}

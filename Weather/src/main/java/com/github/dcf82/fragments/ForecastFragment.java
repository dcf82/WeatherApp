package com.github.dcf82.fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.dcf82.R;
import com.github.dcf82.adapters.ForestAdapter;
import com.github.dcf82.beans.ForestRequest;
import com.github.dcf82.beans.WeatherDay;
import com.github.dcf82.definitions.Config;
import com.github.dcf82.helpers.Utility;
import com.github.dcf82.interfaces.WeatherInterface;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author David Castillo Fuentes
 * This Fragment holds the code to handle Forecast information for the next 7 days based on the
 * current location of the user
 */
public class ForecastFragment extends BaseFragment {

    // RecyclerView
    RecyclerView mRecyclerView;

    // Debug variable
    private static final String LOG = WeatherFragment.class.getName();

    // Current data fetched
    private ForestRequest mForestInfo;

    // Forest Adapter
    private ForestAdapter mAdapter;

    // Buffer
    List<WeatherDay> mWeatherInWeek = new ArrayList<>();

    public ForecastFragment() {
    }

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_fragment, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        // Progress bar
        mWaitBar = (ProgressBar)getView().findViewById(R.id.bar);

        // Recycler View
        mRecyclerView = (RecyclerView)getView().findViewById(R.id.forest_list);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (bundle != null) {
            mLocation = bundle.getParcelable("currentLocation");
            mCurrentOrientation = bundle.getInt("currentOrientation");
            mForestInfo = bundle.getParcelable("currentForestInfo");
        } else {
            mCurrentOrientation = currentOrientation;
        }

        // Verify is there was an orientation change
        mOrientationChange =  mCurrentOrientation != currentOrientation;

        // Capture the current orientation
        mCurrentOrientation = currentOrientation;

        // Create location request
        createLocationRequest();

        // Build Google API
        mGoogleApi = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Connect to Google Places
        mGoogleApi.connect();

        // Get my current location
        getMyLastKnownLocation();

        // Build adapter
        mAdapter = new ForestAdapter(mWeatherInWeek);

        // Fill in data
        if (mForestInfo != null) {

            // Set title
            setTitle(mForestInfo.getCity().getName());

            // Add the items
            mAdapter.setItems(mForestInfo.getWeatherDays());

        }

        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void requestData() {
        if (mLocation != null && !mIsUpdating) {
            updateData(true);
            updateRequest();
        }
    }

    /**
     * Request to the web service
     */
    private void updateRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherInterface weatherInt = retrofit.create(WeatherInterface.class);

        Call<ForestRequest> call = weatherInt.getForecastInfo(mLocation.getLatitude(), mLocation
                .getLongitude(), Config.UNITS);

        call.enqueue(new Callback<ForestRequest>() {
            @Override
            public void onResponse(Response<ForestRequest> response, Retrofit retrofit) {
                // Verify is fragment is added
                if (!isAdded()) return;

                // Synchronization finished
                updateData(false);

                // Update data
                mForestInfo = response.body();

                // Set title
                setTitle(mForestInfo.getCity().getName());

                // Update data
                mAdapter.setItems(mForestInfo.getWeatherDays());
            }

            @Override
            public void onFailure(Throwable t) {

                // Synchronization finished
                updateData(false);

                // Error message
                Utility.showMessage(getActivity(), R.string.network_error);
            }

        });

    }

    @Override
    protected void onLocationChange(Location location) {
        Log.d(LOG, "Location change");

        // Capture the new location
        mLocation = location;

        // Stop making requests
        stopLocationUpdates();

         // Update the data
        if (!mOrientationChange) {
            requestData();
        }

        // Clean up
        mOrientationChange = false;
    }

    /**
     * Store some properties in case of getting notification changes
     */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable("currentForestInfo", mForestInfo);
        super.onSaveInstanceState(bundle);
    }

}

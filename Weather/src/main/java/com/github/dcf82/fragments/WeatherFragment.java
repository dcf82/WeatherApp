package com.github.dcf82.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.dcf82.R;
import com.github.dcf82.beans.WeatherRequest;
import com.github.dcf82.definitions.Config;
import com.github.dcf82.helpers.Utility;
import com.github.dcf82.interfaces.WeatherInterface;
import com.github.dcf82.map.MyInfoWindowAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author David Castillo Fuentes
 * This Fragment holds the code to handle Weather information based on the current location of
 * the user on a MapView, and the user it able to navigate on the map and tap at any location on
 * it and get weather information of that specific location
 */
public class WeatherFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    // Debug variable
    private static final String LOG = WeatherFragment.class.getName();

    // Current instance of Map
    private GoogleMap mMap;

    // Current marker on the Map
    private Marker mMarker;

    // This contains the weather information of the current location selected, which is not
    // necessarily the same like the user's current location, like happens for example when the
    // user clicks at any other point on the map
    private WeatherRequest mWeatherRequest;

    // This is te current location drawn over the map
    private LatLng mCurrentSelectedPositionInMap;

    // Window Adapter
    private MyInfoWindowAdapter mMyAdapter;

    public WeatherFragment() {}

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapview_fragment, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        // Progress bar
        mWaitBar = (ProgressBar)getView().findViewById(R.id.bar);

        // Getting the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (bundle != null) {
            mCurrentSelectedPositionInMap = bundle.getParcelable("currentPositionSelected");
            mWeatherRequest = bundle.getParcelable("currentWeather");
            mLocation = bundle.getParcelable("currentLocation");
            mCurrentOrientation = bundle.getInt("currentOrientation");
        } else {
            mCurrentOrientation = currentOrientation;
        }

        // Verify is there was an orientation change
        mOrientationChange =  mCurrentOrientation != currentOrientation;

        // Capture the current orientation
        mCurrentOrientation = currentOrientation;

        if (mWeatherRequest != null) {
            setTitle(mWeatherRequest.getName());
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add listeners
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        // Add the Window Adapter
        mMyAdapter = new MyInfoWindowAdapter(getActivity(), mWeatherRequest);
        mMap.setInfoWindowAdapter(mMyAdapter);

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

        // Set my current location if this is available
        if (!mOrientationChange && mLocation != null) {
            mCurrentSelectedPositionInMap = buildLatLon(mLocation);
        }

        // Draw the selected position on the map
        setCurrentLocation();

    }

    private LatLng buildLatLon(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    protected void setCurrentLocation() {
        // Nothing to do
        if (mCurrentSelectedPositionInMap == null) return;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                /* Sets the location */
                .target(mCurrentSelectedPositionInMap)
                /* Sets the zoom */
                .zoom(14)
                /* Sets the orientation of the camera to east */
                .bearing(90)
                /* Sets the tilt of the camera to 30 degrees */
                .tilt(30)
                /* Creates a CameraPosition from the builder */
                .build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        if (mMarker != null) {
            mMarker.remove();
            mMarker = null;
        }

        mMarker = mMap.addMarker(new MarkerOptions().position(mCurrentSelectedPositionInMap));

        mMyAdapter.openBar(true);
    }

    protected void requestData() {
        if (mCurrentSelectedPositionInMap != null && !mIsUpdating) {
            updateData(true);
            updateRequest();
        }
    }

    /**
     * Request to the web service
     */
    private void updateRequest() {
        // Nothing to do
        if (mCurrentSelectedPositionInMap == null) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherInterface weatherInt = retrofit.create(WeatherInterface.class);

        Call<WeatherRequest> call = weatherInt.getWeatherInfo(mCurrentSelectedPositionInMap.latitude,
                mCurrentSelectedPositionInMap.longitude, Config.UNITS);

        call.enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Response<WeatherRequest> response, Retrofit retrofit) {

                if (!isAdded()) return;

                // Get the data fetched
                mWeatherRequest = response.body();

                // Synchronization finished
                updateData(false);

                // Update data
                mMyAdapter.setWeatherData(mWeatherRequest);

                // Move to location if enabled
                if (mMoveToLocation) {
                    mMoveToLocation = false;
                    setCurrentLocation();
                }

                // Set title
                setTitle(mWeatherRequest.getName());
            }

            @Override
            public void onFailure(Throwable t) {

                // Synchronization finished
                updateData(false);

                // Update data
                mMyAdapter.setWeatherData(mWeatherRequest);

                // Error message
                Utility.showMessage(getActivity(), R.string.network_error);
            }

        });

    }

    public WeatherRequest getWeatherData() {
        return mWeatherRequest;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // If there is a current ongoing request, cancel the operation
        if (mIsUpdating) return;

        // Update the data
        updateData(true);

        mCurrentSelectedPositionInMap = latLng;

        // Print Location
        setCurrentLocation();

        // Get Info
        updateRequest();
    }

    @Override
    public void onLocationChange(Location location) {
        Log.d(LOG, "Location change");

        // Capture the new location
        mLocation = location;

        // Stop making requests
        stopLocationUpdates();

        // Perform a query only if there was no a rotation change
        if (!mOrientationChange) {

            // Set current location
            mCurrentSelectedPositionInMap = buildLatLon(mLocation);

            // Update the data
            requestData();
        }

        // Clean up
        mOrientationChange = false;
    }

    /**
     * Store some properties in case of getting notification changes
     */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable("currentPositionSelected", mCurrentSelectedPositionInMap);
        bundle.putParcelable("currentWeather", mWeatherRequest);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Close the Marker
        marker.hideInfoWindow();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Open the Marker
        marker.showInfoWindow();
        return false;
    }

}

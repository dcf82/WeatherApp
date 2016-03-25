package com.github.dcf82.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.github.dcf82.R;
import com.github.dcf82.definitions.Config;
import com.github.dcf82.dialogs.DialogFragment;
import com.github.dcf82.helpers.Utility;
import com.github.dcf82.location.LocationServicesHelper;

/**
 * @author David Castillo Fuentes
 * This is the base class that contains the base implementation of location services
 * which can be used everywhere, the child class just need to implemente couple of callback
 * methods to get notifications about location changes and other functionalities
 */
public abstract class BaseFragment extends Fragment implements LocationListener, GoogleApiClient
        .ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    // Simple tag for testing
    private static final String LOG =  BaseFragment.class.getName();

    // The app is consuming the Weather Service
    protected boolean mIsUpdating;

    // Current user location
    protected Location mLocation;

    // A progress bar that the client app can provide to notify that something is happening
    // in the background
    protected ProgressBar mWaitBar;

    // This flag notifies if the Google Maps Api is tracking the user's location at this moment
    protected boolean isRequestEnabled;

    // Google Maps API variables
    protected GoogleApiClient mGoogleApi;
    protected LocationRequest mLocRequest;

    // Stores the device current orientation
    protected int mCurrentOrientation;

    // Track orientation change
    protected boolean mOrientationChange;

    // Move to location
    protected boolean mMoveToLocation;

    protected boolean verifyPermissions() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    protected void getPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    Config.REQUEST_LOCATION_SERVICES);
        }

    }

    protected void getMyLastKnownLocation() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager lm = (LocationManager)getActivity().getSystemService(Context
                    .LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {
                mLocation = location;
            } else {
                mLocation = null;
            }

        }

    }

    protected void openLoadBar(boolean open) {
        if (open) {
            mWaitBar.setVisibility(View.VISIBLE);
        } else {
            mWaitBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApi != null && mGoogleApi.isConnected()) {
            mGoogleApi.disconnect();
        }
        Log.d(LOG, "Closing Google api");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Config.REQUEST_LOCATION_SERVICES: {
                if ((grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) || grantResults.length > 1
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates();
                } else {
                    Utility.showMessage(getActivity(), R.string.enable_loc_services);
                }
                return;
            }
        }
    }

    protected boolean isLocationServicesEnabled() {
        boolean enabled = LocationServicesHelper.isLocationEnabled(getActivity());
        if (!enabled && getChildFragmentManager().findFragmentByTag("fragment_popup") == null) {
            DialogFragment dialogFragment = new DialogFragment();
            dialogFragment.setCancelable(false);
            dialogFragment.show(getChildFragmentManager(), "fragment_popup");
        }
        return enabled;
    }

    protected void setTitle (String title) {
        if (!(getActivity() instanceof  AppCompatActivity)) return;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void requestLocationUpdates() {

        if (verifyPermissions()) {
            isRequestEnabled = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApi, mLocRequest, this);
        } else {
            getPermissions();
            return;
        }

        if (!mMoveToLocation && !mOrientationChange) {
            requestData();
        }
    }

    protected void stopLocationUpdates() {
        if (mGoogleApi != null && mGoogleApi.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApi, this);
        }
        isRequestEnabled = false;
    }

    protected void createLocationRequest() {
        mLocRequest = new LocationRequest();
        mLocRequest.setInterval(Config.TIME_REQUEST);
        mLocRequest.setFastestInterval(Config.TIME_FASTEST);
        mLocRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void updateData(boolean update) {
        // Update finished
        mIsUpdating = update;

        // Close progress bar
        openLoadBar(update);
    }

    public void updateLocation() {
        // If an location update is ongoing, we can cancel the request this time
        if (isRequestEnabled) return;

        // Move to location
        mMoveToLocation = true;

        // Make a location request
        requestLocationUpdates();
    }

    /**
     * Store some properties in case of getting notification changes
     */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable("currentLocation", mLocation);
        bundle.putInt("currentOrientation", mCurrentOrientation);
        super.onSaveInstanceState(bundle);
    }

    public void updateLocationSelected() {
        // Make a data refresh
        requestData();
    }

    @Override
    public void onLocationChanged(Location location) {
        onLocationChange(location);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG, "Service running");
        if (isLocationServicesEnabled()) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected abstract void requestData();
    protected abstract void onLocationChange(Location location);
}

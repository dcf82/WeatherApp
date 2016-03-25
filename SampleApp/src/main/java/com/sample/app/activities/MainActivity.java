package com.sample.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.dcf82.fragments.BaseFragment;
import com.github.dcf82.fragments.ForecastFragment;
import com.github.dcf82.fragments.WeatherFragment;
import com.sample.app.R;
import com.sample.app.configs.Config;

/**
 * @author David Castillo Fuentes
 * This is the main activity of the sample app that uses the library module
 * published in jcenter & maven repositories:
 * MapViewFragment: This is a Google MapView Module where the user can navigate & also use its
 * current location to find Weather information at the moment
 * ForecastFragment: This is a simple Recycler View list that presents a list of forecast
 * information for the next 7 days in intervals of each 3 hours in a day
 */
public class MainActivity extends AppCompatActivity {

    // View type
    private int mCurrentFragment = Config.MAP_VIEW;

    // Base Fragment
    BaseFragment mBaseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (savedInstanceState != null) {
            mCurrentFragment = savedInstanceState.getInt(Config.CURRENT_VIEW, Config.MAP_VIEW);
        }

        addFragment();
    }

    private void addFragment() {

        // Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        // Get current fragment
        mBaseFragment = (BaseFragment)fm.findFragmentByTag(Integer.toString(mCurrentFragment));

        // if fragment already exist, just skip the process
        if (mBaseFragment != null) return;

        // Add the new fragment
        if (mCurrentFragment == Config.MAP_VIEW) {
            mBaseFragment = WeatherFragment.newInstance();
        } else {
            mBaseFragment = ForecastFragment.newInstance();
        }

        // Add & animate
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right);
        fragmentTransaction.replace(R.id.container, mBaseFragment, Integer.toString(mCurrentFragment));
        fragmentTransaction.commit();
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(Config.CURRENT_VIEW, mCurrentFragment);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {

        // Add Map or Weather App
        if (mCurrentFragment == Config.MAP_VIEW) {
            menu.findItem(R.id.action_map).setVisible(false);
            menu.findItem(R.id.action_weather).setVisible(true);
        } else {
            menu.findItem(R.id.action_map).setVisible(true);
            menu.findItem(R.id.action_weather).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_map:
                mCurrentFragment = Config.MAP_VIEW;
                addFragment();
                break;
            case R.id.action_weather:
                mCurrentFragment = Config.WEATHER_VIEW;
                addFragment();
                break;
            case R.id.action_move:
                mBaseFragment.updateLocation();
                break;
            case R.id.action_refresh:
                mBaseFragment.updateLocationSelected();
                break;
        }

        if (id == R.id.action_map ||
                id == R.id.action_weather) {
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

}

package com.github.dcf82.definitions;

/**
 * @author David Castillo Fuentes
 */
public class Config {

    // Base URL for the Web Service
    public static final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";

    // Some tags
    public static final String UNITS = "metric";

    public static final int REQUEST_LOCATION_SERVICES = 1;
    public static final long TIME_REQUEST = 8 * 1000L;
    public static final long TIME_FASTEST = 4 * 1000L;

}

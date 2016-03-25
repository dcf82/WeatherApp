# Weather App

To use the library, you need to create a Google Maps Api Key from http://console.developers.google.com and add the api key to your project in the manifest file:<br />

`<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="@string/your_google_maps_key"/>`<br />
    
This is a library module published in **jcenter** repositories & a sample application that consumes the library. The library module gets weather information from a Restful web service and displays data in two different fashions:

-  Google Maps V2: In this form, the user is able to navigate at any location on the map, and the module will fetch weather information for that particular location and the info will be available when the user taps on the current marker on the map.

- A List module is available, where you can find forecast information for the next week, day by day in intervals of 3 hours.


To add the library to your personal project, add the following lines to your project:

In your build.gradle:<br />
**compile 'com.weather.app:WeatherLibrary:1.0.2'**<br />

In Maven:<br />
**`<dependency>`<br />
`  <groupId>com.weather.app</groupId>`<br />
`  <artifactId>WeatherLibrary</artifactId>`<br />
`  <version>1.0.2</version>`<br />
`  <type>pom</type>`<br />
`</dependency>`<br />**

# Sample 1
![](https://github.com/dcf82/WeatherApp/blob/master/img1.png)

# Sample 2
![](https://github.com/dcf82/WeatherApp/blob/master/img2.png)


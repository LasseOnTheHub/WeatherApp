package com.grp8.weatherapp.TestData;

import java.util.ArrayList;

/**
 * Created by lbirk on 22-10-2016.
 */

public final class WeatherStations {

    ArrayList<WeatherStation> weatherStationsArr;

    private static WeatherStations instance = null;

    public static WeatherStations getInstance()
    {
        if(instance == null)
        {
            instance = new WeatherStations();
        }

        return instance;
    }

    private WeatherStations()
    {
        createWeatherStations();
    };

    private void createWeatherStations()
    {

        //Testdata

        weatherStationsArr = new ArrayList<WeatherStation>();
        //Generate WeatherStation test-data
        /**
         *  On initializing a WeatherStation Object, a WeatherData Object will be created
         *  generation poorly random data for the variables (values from 0-100). Re-generate new data
         *  by calling WeatherStation.Weatherdata.generateRandomData()
         */
        WeatherStation ID1 = new WeatherStation(1,"Majs-mark",60.426426,24.377971,5);
        WeatherStation ID2 = new WeatherStation(2,"Korn-mark",60.425362, 24.374703,5);

        weatherStationsArr.add(ID1);
        weatherStationsArr.add(ID2);
    }

    public ArrayList<WeatherStation> getWeatherStations()
    {
        return weatherStationsArr;
    }



}

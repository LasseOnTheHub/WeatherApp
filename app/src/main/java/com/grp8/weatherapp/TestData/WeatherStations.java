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
        WeatherStation ID2 = new WeatherStation(2,"Korn-mark",60.425362, 24.3747029,5);
        WeatherStation ID3 = new WeatherStation(1,"Majs-mark",60.426426,24.377991,5);
        WeatherStation ID4 = new WeatherStation(2,"Korn-mark",60.425362, 24.374703,5);
        WeatherStation ID5 = new WeatherStation(1,"Majs-mark",60.426426,24.377971,5);
        WeatherStation ID6 = new WeatherStation(2,"Korn-mark",60.425362, 24.374703,5);
        WeatherStation ID7 = new WeatherStation(1,"Majs-mark",60.426426,24.377971,5);
        WeatherStation ID8 = new WeatherStation(2,"Korn-mark",60.425362, 24.374793,5);
        WeatherStation ID9 = new WeatherStation(1,"Majs-mark",60.426426,24.377971,5);
        WeatherStation ID10 = new WeatherStation(2,"Korn-mark",60.425362, 24.374703,5);
        WeatherStation ID11 = new WeatherStation(1,"Majs-mark",60.426426,24.377929,5);
        WeatherStation ID12 = new WeatherStation(2,"Korn-mark",60.425362, 24.374703,5);
        WeatherStation ID13 = new WeatherStation(1,"Majs-mark",60.426426,24.377971,5);
        WeatherStation ID14 = new WeatherStation(2,"Korn-mark",60.425362, 24.374703,5);

        weatherStationsArr.add(ID1);
        weatherStationsArr.add(ID2);
        weatherStationsArr.add(ID3);
        weatherStationsArr.add(ID4);
        weatherStationsArr.add(ID5);
        weatherStationsArr.add(ID6);
        weatherStationsArr.add(ID7);
        weatherStationsArr.add(ID8);
        weatherStationsArr.add(ID9);
        weatherStationsArr.add(ID10);
        weatherStationsArr.add(ID11);
        weatherStationsArr.add(ID12);
        weatherStationsArr.add(ID13);
        weatherStationsArr.add(ID14);
    }

    public ArrayList<WeatherStation> getWeatherStations()
    {
        return weatherStationsArr;
    }

}

package com.grp8.weatherapp.TestData;

import java.io.Serializable;

/**
 * Created by lbirk on 21-10-2016.
 */

public class WeatherStation implements Serializable {

    private int ID;
    private String title;
    private  double latitude;
    private double longitude;
    private int ownerID;
    WeatherData weatherData;

    public WeatherStation(int ID,String title, double latitude, double longitude, int ownerID)
    {
        this.ID = ID;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerID = ownerID;
        weatherData = new WeatherData();
        weatherData.generateRandomData();
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

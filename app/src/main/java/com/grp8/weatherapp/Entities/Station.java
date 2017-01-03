package com.grp8.weatherapp.Entities;

/**
 * Created by Thomas on 03-Jan-17.
 */
public class Station
{
    private int id;
    private int type;

    private String notes;

    private double latitude;
    private double longitude;

    public Station(int id, int type, String notes, double latitude, double longitude)
    {
        this.id        = id;
        this.type      = type;
        this.notes     = notes;
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    public int getId()
    {
        return id;
    }

    public int getType()
    {
        return type;
    }

    public String getNotes()
    {
        return notes;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }
}

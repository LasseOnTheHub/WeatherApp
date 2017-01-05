package com.grp8.weatherapp.Entities;

import com.grp8.weatherapp.Entities.Data.*;
import java.util.Date;

/**
 * Created by Thomas on 03-Jan-17.
 */
public class DataReading
{
    private int  id;
    private Date date;

    private double rain;

    private Air air;
    private Wind wind;
    private Soil soil;

    public DataReading(int id, int timestamp, double rain, Air air, Wind wind, Soil soil)
    {
        this.id   = id;
        this.date = new Date((long) timestamp * 1000);

        this.rain = rain;
        this.air  = air;
        this.wind = wind;
        this.soil = soil;
    }

    public int getID()
    {
        return this.id;
    }

    public Date getTimestamp()
    {
        return this.date;
    }

    public double getRainfall()
    {
        return this.rain;
    }

    public Air getAirReadings()
    {
        return this.air;
    }

    public Wind getWindReadings()
    {
        return this.wind;
    }

    public Soil getSoilReadings()
    {
        return this.soil;
    }
}

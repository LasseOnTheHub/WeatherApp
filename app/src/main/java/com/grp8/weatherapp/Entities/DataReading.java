package com.grp8.weatherapp.Entities;

import com.grp8.weatherapp.Entities.Data.*;
import java.util.Date;

/**
 * Created by Thomas on 03-Jan-17.
 */
public class DataReading
{
    private final int id;
    private final int device;
    private Date date;

    private final double rain;

    private final Air air;
    private final Wind wind;
    private final Soil soil;

    public DataReading(int id, int device, Date date, double rain, Air air, Wind wind, Soil soil)
    {
        this.id     = id;
        this.device = device;
        this.date   = date;

        this.rain = rain;
        this.air  = air;
        this.wind = wind;
        this.soil = soil;
    }

    public int getID()
    {
        return this.id;
    }

    public int getDeviceID()
    {
        return this.device;
    }

    public void setTimestamp(Date date)
    {
        this.date = date;
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

package com.grp8.weatherapp.Data.Cache;

import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;

import java.util.Date;

/**
 * Created by t_bit on 16-01-2017.
 */

public class CacheEntry
{
    private Station     station;
    private DataReading reading;

    public CacheEntry(Station station, DataReading reading)
    {
        this.station = station;
        this.reading = reading;
    }

    public Station getStation()
    {
        return this.station;
    }

    public DataReading getReading()
    {
        return this.reading;
    }

    public void setReading(DataReading reading)
    {
        this.reading = reading;
    }

    public Date getReadingTimestamp()
    {
        return this.reading.getTimestamp();
    }

}

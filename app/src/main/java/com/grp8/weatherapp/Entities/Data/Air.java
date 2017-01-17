package com.grp8.weatherapp.Entities.Data;

/**
 * Created by t_bit on 05-01-2017.
 */
public class Air
{
    private final int pressure;
    private final int humidity;
    private final double temperature;

    public Air(int pressure, int humidity, double temperature)
    {
        this.pressure = pressure;
        this.humidity = humidity;

        this.temperature = temperature;
    }

    /**
     * @return Air pressure in BAR
     */
    public int getPressure()
    {
        return pressure;
    }

    /**
     * @return Relative air humidity
     */
    public int getHumidity()
    {
        return humidity;
    }

    /**
     * @return Air temperature
     */
    public double getTemperature()
    {
        return temperature;
    }
}

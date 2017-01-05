package com.grp8.weatherapp.Entities.Data;

/**
 * Created by t_bit on 05-01-2017.
 */

public class Soil
{
    private int[] moisture;
    private int[] temperature;

    public Soil(int[] moisture, int[] temperature)
    {
        this.moisture    = moisture;
        this.temperature = temperature;
    }

    /**
     * @return An array of moisture readings for zero or more stations.
     */
    public int[] getMoisture()
    {
        return this.moisture;
    }

    /**
     * @return An array of temperature readings for zero or more stations.
     */
    public int[] getTemperature()
    {
        return this.temperature;
    }

}

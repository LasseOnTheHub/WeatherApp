package com.grp8.weatherapp.Entities.Data;

/**
 * Created by t_bit on 05-01-2017.
 */
public class Wind
{
    private double speed;
    private int    direction;

    public Wind(double speed, int direction)
    {
        this.speed     = speed;
        this.direction = direction;
    }

    /**
     * @return Wind speed;
     */
    public double getSpeed()
    {
        return this.speed;
    }

    /**
     * TODO: Figure out how the integer represents direction?!
     *
     * @return Wind direction.
     */
    public int getDirection()
    {
        return this.direction;
    }
}

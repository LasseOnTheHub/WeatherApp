package com.grp8.weatherapp.SupportingFiles.Converters;

/**
 * Created by Henrik on 09-01-2017.
 */
public class TemperatureConverter {

    public double toFahrenheit(double celcius) {
    celcius = (celcius * 9/5.0) +32;
        return celcius;
    }
}

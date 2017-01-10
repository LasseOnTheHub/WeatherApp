package com.grp8.weatherapp.SupportingFiles.Converters;

import android.content.Context;

import com.grp8.weatherapp.Model.SettingsManager;
import com.grp8.weatherapp.R;

/**
 * Created by Henrik on 09-01-2017.
 */
public class TemperatureConverter {

    public static double getFormattedTemp(Context context, double temperature) {
        String[] tempArray = context.getResources().getStringArray(R.array.temp_options_values);
        String userUnit = SettingsManager.getTempUnit(context);
        if (tempArray[0].equals(userUnit)) {
            // Celsius
            return temperature;
        } else {
            return (temperature * 9 / 5.0) + 32;
        }
    }

}

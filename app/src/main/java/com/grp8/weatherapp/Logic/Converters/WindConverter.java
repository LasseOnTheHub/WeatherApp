package com.grp8.weatherapp.Logic.Converters;

import android.content.Context;

import com.grp8.weatherapp.Logic.SettingsManager;
import com.grp8.weatherapp.R;

/**
 * Created by Frederik on 18/01/2017.
 */

public class WindConverter {

    public static double getFormattedWind(Context context, double wind) {
        String[] windArray = context.getResources().getStringArray(R.array.windspeed_values);
        String userUnit = SettingsManager.getWindSpeedUnit(context);

        if (userUnit.equals(windArray[1])) {
            // Meters/hour
            return wind*60*60;
        } else if (userUnit.equals(windArray[2])) {
            // kilometers/second
            return wind/1000;
        } else if (userUnit.equals(windArray[3])) {
            // kilometers/hour
            return wind*60*60/1000;
        } else if (userUnit.equals(windArray[4])) {
            // miles/hour
            return wind*2.23694;
        } else {
            return wind;
        }
    }
}

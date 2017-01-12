package com.grp8.weatherapp.Logic.Converters;

import android.content.Context;

import com.grp8.weatherapp.Logic.SettingsManager;
import com.grp8.weatherapp.R;

/**
 * Created by Henrik on 09-01-2017.
 */
public class PressureConverter {

    public static double getFormattedPressure(Context context, double pres) {
        String[] tempArray = context.getResources().getStringArray(R.array.pressure_options);
        String userUnit = SettingsManager.getTempUnit(context);

        if (tempArray[1].equals(userUnit)) {
            // Bar
            return pres / 1000;
        } else if (tempArray[2].equals(userUnit)) {
            // atm
            return pres / 1013.25;
        } else if (tempArray[3].equals(userUnit)) {
            // mmHg
            return pres / 68.94757293168;
        } else {
            return pres;
        }
    }
}


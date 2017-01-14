package com.grp8.weatherapp.Logic;

import android.content.Context;
import android.preference.PreferenceManager;

import com.grp8.weatherapp.R;

/**
 * Created by Frederik on 07/01/2017.
 */

public class SettingsManager {

    public static String getTempUnit(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(Constants.KEY_TEMP_UNIT, context.getResources().getStringArray(R.array.temp_options_values)[0]);
    }

    public static String getPressureUnit(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(Constants.KEY_PRESS_UNIT, "");

    }

    public static String getWindSpeedUnit(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(Constants.KEY_WS_UNIT, "");
    }

    public static String getLanguage(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(Constants.KEY_LANG, "");
    }

    public static void setupSettings(Context context) {
        if (getTempUnit(context).equals("")) {
            String temp = context.getResources().getStringArray(R.array.temp_options_values)[0];
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putString(Constants.KEY_TEMP_UNIT, temp)
                    .apply();

        }
        if (getPressureUnit(context).equals("")) {
            String press = context.getResources().getStringArray(R.array.pressure_options)[0];
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putString(Constants.KEY_PRESS_UNIT, press)
                    .apply();
        }
        if (getWindSpeedUnit(context).equals("")) {
            String ws = context.getResources().getStringArray(R.array.windspeed_values)[0];
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putString(Constants.KEY_WS_UNIT, ws)
                    .apply();
        }
        if (getLanguage(context).equals("")) {
            String lang = context.getResources().getStringArray(R.array.language_options)[0];
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putString(Constants.KEY_LANG, lang)
                    .apply();
        }

    }
}

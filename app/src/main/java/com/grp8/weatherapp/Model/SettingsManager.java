package com.grp8.weatherapp.Model;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.grp8.weatherapp.SupportingFiles.Constants;

/**
 * Created by Frederik on 07/01/2017.
 */

public class SettingsManager {

    Context context = Application.getApplicationContext();

    public static String getTempUnit() {
        PreferenceManager
                .getDefaultSharedPreferences(Application.getApplicationContext())
                .getString(Constants.KEY_TEMP_UNIT, "");

        return "";
    }
}

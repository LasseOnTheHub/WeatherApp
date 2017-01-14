package com.grp8.weatherapp.Logic;

/**
 * Created by lbirk on 05-01-2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.grp8.weatherapp.Entities.DataReading;

public class Utils
{
    public static boolean isEmulator()
    {
        return Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator");
    }

    public static boolean isNetworkAvailable(Context context)
    {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            return netInfo != null && netInfo.isConnected();
    }

    public static boolean sanityCheck(DataReading reading) {
        return Math.abs(reading.getAirReadings().getTemperature())>50 ||
                reading.getAirReadings().getHumidity()>100 ||
                reading.getAirReadings().getHumidity()<0 ||
                reading.getWindReadings().getSpeed()>45;
    }
}

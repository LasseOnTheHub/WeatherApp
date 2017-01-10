package com.grp8.weatherapp.SupportingFiles;

/**
 * Created by lbirk on 05-01-2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

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
}

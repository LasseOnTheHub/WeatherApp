package com.grp8.weatherapp.Activities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grp8.weatherapp.TestData.WeatherStation;

import java.util.ArrayList;

/**
 * Created by Frederik on 23/10/2016.
 */

public class WeatherStationAdapter extends ArrayAdapter {

    private static class ViewHolder {
        private TextView stationTitle;
        private TextView timeLabel;
        private TextView tempLabel;
    }

    public WeatherStationAdapter(Context context, int textViewResourceId, ArrayList<WeatherStation> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

    }






}

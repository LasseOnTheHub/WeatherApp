package com.grp8.weatherapp.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Frederik on 23/10/2016.
 */

public class WeatherStationAdapter extends ArrayAdapter {

    private static class ViewHolder {
        TextView stationTitle;
        TextView timeLabel;
        TextView tempLabel;
    }

    public WeatherStationAdapter(Context context, int listResourceId, ArrayList<WeatherStation> items) {
        super(context, listResourceId, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WeatherStation station = (WeatherStation) getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stationlistelement, parent, false);
            viewHolder.stationTitle = (TextView) convertView.findViewById(R.id.title_label);
            viewHolder.timeLabel = (TextView) convertView.findViewById(R.id.time_label);
            viewHolder.tempLabel = (TextView) convertView.findViewById(R.id.temp_label);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.stationTitle.setText(station.getTitle());
        viewHolder.timeLabel.setText("13:52");
        viewHolder.tempLabel.setText(String.valueOf(station.getWeatherData().getAirTemp())+"º");

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}

package com.grp8.weatherapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.util.ArrayList;

/**
 * Created by Frederik on 14/11/2016.
 */

public class WeatherStationsAdapter extends BaseAdapter {

    private WeatherStations weatherStations;
    private LayoutInflater inflater;

    public WeatherStationsAdapter(Activity activity) {
        super();
        weatherStations = WeatherStations.getInstance();
        inflater = activity.getLayoutInflater();
    }

    private static class ViewHolder {
        TextView stationTitle;
        TextView timeLabel;
        TextView tempLabel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WeatherStation station = (WeatherStation) getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.stationlistelement, parent, false);
            viewHolder.stationTitle = (TextView) convertView.findViewById(R.id.title_label);
            viewHolder.timeLabel = (TextView) convertView.findViewById(R.id.time_label);
            viewHolder.tempLabel = (TextView) convertView.findViewById(R.id.temp_label);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.stationTitle.setText(station.getTitle());
        viewHolder.timeLabel.setText("11:52");
        if (position%2==0) {
            viewHolder.timeLabel.setText("13:52");
        }
        viewHolder.tempLabel.setText(String.valueOf(station.getWeatherData().getAirTemp())+"º");

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        // Missing implementation
        return 0;
    }

    @Override
    public int getCount() {
        return weatherStations.getWeatherStations().size();
    }

    @Override
    public Object getItem(int position) {
        return weatherStations.getWeatherStations().get(position);
    }

}

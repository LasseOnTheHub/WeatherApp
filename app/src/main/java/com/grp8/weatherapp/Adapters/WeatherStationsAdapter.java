package com.grp8.weatherapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.SupportingFiles.Constants;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Frederik on 14/11/2016.
 */

public class WeatherStationsAdapter extends BaseAdapter {

    private WeatherStations weatherStations;
    private LayoutInflater inflater;
    private Activity activity;

    public WeatherStationsAdapter(Activity activity) {
        super();
        weatherStations = WeatherStations.getInstance();
        inflater = activity.getLayoutInflater();
        this.activity = activity;
    }

    private static class ViewHolder {
        TextView stationTitle;
        TextView timeLabel;
        TextView tempLabel;
        LinearLayout oldContent;
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
            viewHolder.oldContent = (LinearLayout) convertView.findViewById(R.id.old_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.stationTitle.setText(station.getTitle());
        if (position%2==0) {
            Date date = new Date(System.currentTimeMillis()-2760000);
            viewHolder.timeLabel.setText(formatDate(date));
            viewHolder.oldContent.setVisibility(LinearLayout.VISIBLE);
        } else {
            Date date = new Date();
            viewHolder.timeLabel.setText(formatDate(date));
            viewHolder.oldContent.setVisibility(LinearLayout.GONE);
        }

        String tempUnit = PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.KEY_TEMP_UNIT,"");
        viewHolder.tempLabel.setText(String.valueOf(station.getWeatherData().getAirTemp())+tempUnit);

        return convertView;
    }

    private String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String time;
        if (cal.get(Calendar.DAY_OF_MONTH) <= 9) {
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d HH:mm");
            time = formatter.format(date);
        } else {
            time = date.toString().substring(4,16);
        }
        return time;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return weatherStations.getWeatherStations().get(position).getID();
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

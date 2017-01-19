package com.grp8.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Logic.Converters.TemperatureConverter;
import com.grp8.weatherapp.Logic.SettingsManager;
import com.grp8.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Frederik on 16/01/2017.
 */

public class WeatherStationAdapter extends BaseAdapter {

    private final List<Station> stations;
    private final List<DataReading> readings;
    private final SimpleDateFormat formatter = new SimpleDateFormat("MMM d HH:mm yyyy", Locale.getDefault());
    private final LayoutInflater inflater;
    private final Context context;

    public WeatherStationAdapter(List<Station> stations, List<DataReading> readings, LayoutInflater inflater, Context context) {
        super();
        this.stations = stations;
        this.readings = readings;
        this.inflater = inflater;
        this.context = context;
    }

    private static class ViewHolder {
        TextView stationTitle, timeLabel, tempLabel;
        LinearLayout timeLayout, oldContent, errorLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder              = new ViewHolder();
            convertView             = inflater.inflate(R.layout.stationlistelement, parent, false);
            viewHolder.stationTitle = (TextView) convertView.findViewById(R.id.title_label);
            viewHolder.timeLabel    = (TextView) convertView.findViewById(R.id.time_label);
            viewHolder.tempLabel    = (TextView) convertView.findViewById(R.id.temp_label);
            viewHolder.timeLayout   = (LinearLayout) convertView.findViewById(R.id.timeLayout);
            viewHolder.oldContent   = (LinearLayout) convertView.findViewById(R.id.old_content);
            viewHolder.errorLayout  = (LinearLayout) convertView.findViewById(R.id.error_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (readings.size() < position
                || readings.get(position) == null
                || stations.size() < position
                || stations.get(position) == null) {
            showErrorState(viewHolder, stations.get(position));
            return convertView;
        }

        viewHolder.oldContent.setVisibility(View.GONE);
        viewHolder.errorLayout.setVisibility(View.GONE);

        configureView(viewHolder, position);

        return convertView;
    }

    private void configureView(ViewHolder viewHolder, int position) {
        setTempLabel(viewHolder, readings.get(position));
        setTimeLabel(viewHolder, readings.get(position));
        viewHolder.stationTitle.setText(stations.get(position).getNotes());
    }

    private void setTempLabel(ViewHolder viewHolder, DataReading reading) {
        String convertedTemp = String.format(Locale.getDefault(), "%.2f", TemperatureConverter.getFormattedTemp(context, reading.getAirReadings().getTemperature()));
        viewHolder.tempLabel.setVisibility(View.VISIBLE);
        String temp = convertedTemp + SettingsManager.getTempUnit(context);
        viewHolder.tempLabel.setText(temp);
    }

    private void setTimeLabel(ViewHolder viewHolder, DataReading reading) {
        viewHolder.timeLabel.setVisibility(View.VISIBLE);
        viewHolder.timeLabel.setText(formatDate(reading.getTimestamp()));
        viewHolder.timeLayout.setVisibility(View.VISIBLE);
        viewHolder.oldContent.setVisibility(isOldContent(reading.getTimestamp()) ? View.VISIBLE : View.GONE);
        // TODO Sane content
    }

    /**
     * Returns a date in the format "MMM d HH:mm yyyy" i.e. "Jan 6 12:34 1989"
     * @param date
     * @return A formatted String representation of the date.
     */
    private String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String time;
        if (cal.get(Calendar.DAY_OF_MONTH) <= 9) {
            time = formatter.format(date);
        } else {
            time = date.toString().substring(4,16);
        }
        return time;
    }

    /**
     * A boolean indicating whether the date is older than 30 minutes
     * @param date - The date to be evaluated
     * @return True if the data is older than 30 minutes, otherwise false
     */
    private boolean isOldContent(Date date) {
        /*
         *  30 minutes = 30*60*1000 = 1.800.000 milliseconds
         *  System.currentTimeMillis is relative to the time and date of the device (which may be an emulator)
         *  - if so, the time may be off, if not set correctly in settings
         */
        Date threshold = new Date(System.currentTimeMillis()-1800000);
        return date.before(threshold);
    }

    private void showErrorState(ViewHolder viewHolder, Station station) {
        viewHolder.tempLabel.setVisibility(View.INVISIBLE); // Label for temperature
        viewHolder.timeLayout.setVisibility(View.INVISIBLE); // Layout for time (including old content)
        if (station == null) {
            viewHolder.stationTitle.setVisibility(View.GONE); // Label for station title
        } else {
            viewHolder.stationTitle.setVisibility(View.VISIBLE); // Label for station title
            viewHolder.stationTitle.setText(station.getNotes()); // Setting station title
        }
        viewHolder.errorLayout.setVisibility(View.VISIBLE); // Label for error
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stations.get(position).getId();
    }
}

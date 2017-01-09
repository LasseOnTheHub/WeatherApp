package com.grp8.weatherapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grp8.weatherapp.Data.DataRepository;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Model.SettingsManager;
import com.grp8.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Frederik on 14/11/2016.
 */

public class WeatherStationsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private int id;
    private int count;
    private Station item;

    public WeatherStationsAdapter(Activity activity) {
        super();
        inflater = activity.getLayoutInflater();
        this.activity = activity;
        DataRepositoryFactory.build(activity.getApplicationContext()).setUser(5);
    }

    private static class ViewHolder {
        TextView stationTitle;
        TextView timeLabel;
        TextView tempLabel;
        LinearLayout oldContent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Station station = (Station) getItem(position);

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

        if (station == null) {
            viewHolder.stationTitle.setText("Some title");
        } else {
            viewHolder.stationTitle.setText(station.getNotes());
        }
        if (position % 2 == 0) {
            Date date = new Date(System.currentTimeMillis()-2760000);
            viewHolder.timeLabel.setText(formatDate(date));
            viewHolder.oldContent.setVisibility(LinearLayout.VISIBLE);
        } else {
            Date date = new Date();
            viewHolder.timeLabel.setText(formatDate(date));
            viewHolder.oldContent.setVisibility(LinearLayout.GONE);
        }

        String tempUnit = SettingsManager.getTempUnit(activity.getApplicationContext());
        String temp = /*String.valueOf(station.getWeatherData().getAirTemp())+*/tempUnit;
        viewHolder.tempLabel.setText(temp);

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

    private boolean isOldContent(Date date) {
        Date threshold = new Date(System.currentTimeMillis()-1800000);
        return date.compareTo(threshold) == 0 || date.compareTo(threshold) == 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(final int position) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... arg0) {
                try {
                    return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                if (result != null) {
                    id = (int) result;
                }
            }
        }.execute();
        return id;
    }

    @Override
    public int getCount() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... arg0) {
                try {
                    return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().size();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                if (result != null) {
                    count = (int) result;
                }
            }
        }.execute();
        return count;
    }

    @Override
    public Object getItem(final int position) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... arg0) {
                try {
                    return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().get(position);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                if (result != null) {
                    item = (Station) result;
                }
            }
        }.execute();
        return item;
    }

}

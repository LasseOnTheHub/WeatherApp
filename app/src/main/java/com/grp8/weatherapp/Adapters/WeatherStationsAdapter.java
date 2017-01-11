package com.grp8.weatherapp.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.grp8.weatherapp.Data.DataRepository;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Model.SettingsManager;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.SupportingFiles.Utils;

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
    private ListView list;

    private enum ViewState { Error, Loading, Loaded, LoadedOldContent, LoadedInsaneContent }

    public WeatherStationsAdapter(Activity activity, ListView list) {
        super();
        inflater = activity.getLayoutInflater();
        this.activity = activity;
        this.list = list;
    }

    private static class ViewHolder {
        TextView stationTitle;
        TextView timeLabel;
        TextView tempLabel;
        LinearLayout timeLayout;
        LinearLayout oldContent;
        LinearLayout errorLayout;
        ProgressBar tempSpinner;
        ProgressBar timeSpinner;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Station station = (Station) getItem(position);

        if (station != null && convertView == null) {
            // First load of element and station is not null
            new AsyncTask<Void,DataReading,DataReading>() {
                @Override
                protected DataReading doInBackground(Void... arg0) {
                    try {
                        return DataRepositoryFactory.build(activity.getApplicationContext()).getStationData(station.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(DataReading result) {
                    updateListItem(position, result);
                }
            }.execute();
        }

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.stationlistelement, parent, false);
            viewHolder.stationTitle = (TextView) convertView.findViewById(R.id.title_label);
            viewHolder.timeLabel = (TextView) convertView.findViewById(R.id.time_label);
            viewHolder.tempLabel = (TextView) convertView.findViewById(R.id.temp_label);
            viewHolder.timeLayout = (LinearLayout) convertView.findViewById(R.id.timeLayout);
            viewHolder.oldContent = (LinearLayout) convertView.findViewById(R.id.old_content);
            viewHolder.errorLayout = (LinearLayout) convertView.findViewById(R.id.error_layout);
            viewHolder.tempSpinner = (ProgressBar) convertView.findViewById(R.id.spinner_temp);
            viewHolder.timeSpinner = (ProgressBar) convertView.findViewById(R.id.spinner_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (station == null) {
            viewHolder.stationTitle.setText("Some title");
        } else {
            viewHolder.stationTitle.setText(station.getNotes());
        }

        return convertView;
    }

    private void updateListItem(int index, DataReading reading) {
        View element = list.getChildAt(index);

        if (element == null) {
            return;
        }

        ViewHolder viewHolder = (ViewHolder) element.getTag();
        if (reading != null) {
            viewHolder.tempSpinner.setVisibility(View.GONE);
            viewHolder.timeSpinner.setVisibility(View.GONE);
            viewHolder.timeLabel.setVisibility(View.VISIBLE);
            viewHolder.tempLabel.setVisibility(View.VISIBLE);
            String temp = String.valueOf(reading.getAirReadings().getTemperature()) + SettingsManager.getTempUnit(activity.getApplicationContext());
            viewHolder.tempLabel.setText(temp);
            viewHolder.timeLabel.setText(formatDate(reading.getTimestamp()));
            if (isOldContent(reading.getTimestamp())) {
                viewHolder.oldContent.setVisibility(View.VISIBLE);
            } else if (Utils.sanityCheck(reading)) {
                viewHolder.oldContent.setVisibility(View.VISIBLE);
            } else {
                viewHolder.oldContent.setVisibility(View.GONE);
            }
        } else {
            viewHolder.tempSpinner.setVisibility(View.GONE);
            viewHolder.timeSpinner.setVisibility(View.GONE);
            viewHolder.timeLayout.setVisibility(View.GONE);
            viewHolder.errorLayout.setVisibility(View.VISIBLE);
        }
    }

    private String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String time;
        if (cal.get(Calendar.DAY_OF_MONTH) <= 9) {
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d HH:mm yyyy");
            time = formatter.format(date);
        } else {
            time = date.toString().substring(4,16);
        }
        return time;
    }

    private void showState(View element, ViewHolder viewHolder, DataReading reading, ViewState state) {
        switch (state) {
            case Error:
                viewHolder.tempLabel.setVisibility(View.GONE); // Label for temp
                viewHolder.timeLayout.setVisibility(View.GONE); // Layout for time (including old content)
                viewHolder.tempSpinner.setVisibility(View.GONE); // Spinner for temp (big one)
                viewHolder.timeSpinner.setVisibility(View.GONE); // Spinner for time (little one)
                viewHolder.stationTitle.setVisibility(View.VISIBLE); // Always visible
                viewHolder.errorLayout.setVisibility(View.VISIBLE); // Label for error
                // Only error layout and station name is visible
                return;
            case Loading:
                viewHolder.tempLabel.setVisibility(View.GONE); // Label for temp
                viewHolder.oldContent.setVisibility(View.GONE); // Layout for oldContent
                viewHolder.timeLabel.setVisibility(View.GONE); // Label for time
                viewHolder.errorLayout.setVisibility(View.GONE); // Layout for error
                viewHolder.tempSpinner.setVisibility(View.VISIBLE); // Spinner for temp
                viewHolder.timeSpinner.setVisibility(View.VISIBLE); // Spinner for time
                viewHolder.timeLayout.setVisibility(View.VISIBLE); // Layout for time
                viewHolder.stationTitle.setVisibility(View.VISIBLE); // Always visible
                // Spinners, clock image and title is visible
                break;
            case Loaded:
                viewHolder.tempLabel.setVisibility(View.VISIBLE); // Label for temp
                viewHolder.stationTitle.setVisibility(View.VISIBLE); // Always visible
                viewHolder.timeLabel.setVisibility(View.VISIBLE); // Label for time
                viewHolder.timeLayout.setVisibility(View.VISIBLE); // Layout for time
                viewHolder.errorLayout.setVisibility(View.GONE); // Layout for error
                viewHolder.oldContent.setVisibility(View.GONE); // Layout for old content
                viewHolder.tempSpinner.setVisibility(View.GONE); // Spinner for temp
                viewHolder.timeSpinner.setVisibility(View.GONE); // Spinner for time
                break;
            case LoadedOldContent:
                viewHolder.tempLabel.setVisibility(View.VISIBLE); // Label for temp
                viewHolder.stationTitle.setVisibility(View.VISIBLE); // Always visible
                viewHolder.timeLabel.setVisibility(View.VISIBLE); // Label for time
                viewHolder.timeLayout.setVisibility(View.VISIBLE); // Layout for time
                viewHolder.oldContent.setVisibility(View.VISIBLE); // Layout for old content
                viewHolder.errorLayout.setVisibility(View.GONE); // Layout for error
                viewHolder.tempSpinner.setVisibility(View.GONE); // Spinner for temp
                viewHolder.timeSpinner.setVisibility(View.GONE); // Spinner for time
                break;
            case LoadedInsaneContent:
                viewHolder.tempLabel.setVisibility(View.VISIBLE); // Label for temp
                viewHolder.stationTitle.setVisibility(View.VISIBLE); // Always visible
                viewHolder.timeLabel.setVisibility(View.VISIBLE); // Label for time
                viewHolder.timeLayout.setVisibility(View.VISIBLE); // Layout for time
                viewHolder.oldContent.setVisibility(View.VISIBLE); // Layout for old content
                // TODO Set insane content layout
                viewHolder.errorLayout.setVisibility(View.GONE); // Layout for error
                viewHolder.tempSpinner.setVisibility(View.GONE); // Spinner for temp
                viewHolder.timeSpinner.setVisibility(View.GONE); // Spinner for time
                break;
        }
        // TODO Fill labels
    }

    private boolean isOldContent(Date date) {
        Date threshold = new Date(System.currentTimeMillis()-1800000);
        return date.before(threshold);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(final int position) {
        return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().get(position).getId();
    }

    @Override
    public int getCount() {
        return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().size();
    }

    @Override
    public Object getItem(final int position) {
        return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().get(position);
    }

}

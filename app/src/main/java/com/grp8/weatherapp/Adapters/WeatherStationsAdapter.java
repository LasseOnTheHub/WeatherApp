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

        ViewHolder viewHolder;

        if (convertView == null) {
            // First load of element
            if (station != null) {
                new AsyncTask<Void, DataReading, DataReading>() {
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
            viewHolder = new ViewHolder();
            // Inflating convertView and retrieving the outlets
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
            // Setting the view state to error
            showErrorState(viewHolder, null);
            // Only error layout is visible
            return convertView;
        }

        // Setting up outlets for loading screen
        viewHolder.tempLabel.setVisibility(View.GONE); // Label for temperature
        viewHolder.oldContent.setVisibility(View.GONE); // Layout for oldContent
        viewHolder.timeLabel.setVisibility(View.GONE); // Label for time
        viewHolder.errorLayout.setVisibility(View.GONE); // Layout for error
        viewHolder.tempSpinner.setVisibility(View.VISIBLE); // Spinner for temperature
        viewHolder.timeSpinner.setVisibility(View.VISIBLE); // Spinner for time
        viewHolder.timeLayout.setVisibility(View.VISIBLE); // Layout for time
        viewHolder.stationTitle.setVisibility(View.VISIBLE); // Label for station title
        viewHolder.stationTitle.setText(station.getNotes()); // Setting the stations as title
        // Spinners, clock image and title is visible

        return convertView;
    }

    /**
     * Updates a specific item in the list with the data provided in the parameter index.
     * @param index The index of the list item that is to be updated
     * @param reading The data reading that should populate the list item
     */
    private void updateListItem(int index, DataReading reading) {
        View element = list.getChildAt(index);

        if (element == null) {
            // Element is no longer on screen and will update the next time getView is called
            return;
        }

        // Element is not null, which means the view has been on screen and has set up outlets
        ViewHolder viewHolder = (ViewHolder) element.getTag();

        if (reading == null) {
            // Some kind of error occurred
            showErrorState(viewHolder, (Station) getItem(index));
            // Only error layout and station name is visible
            return;
        }

        // Setting outlets
        setTempLabel(viewHolder, reading);
        setTimeLabel(viewHolder, reading);
        viewHolder.stationTitle.setText(((Station) getItem(index)).getNotes());
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
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d HH:mm yyyy");
            time = formatter.format(date);
        } else {
            time = date.toString().substring(4,16);
        }
        return time;
    }

    private void showErrorState(ViewHolder viewHolder, Station station) {
        viewHolder.tempLabel.setVisibility(View.GONE); // Label for temperature
        viewHolder.timeLayout.setVisibility(View.GONE); // Layout for time (including old content)
        viewHolder.tempSpinner.setVisibility(View.GONE); // Spinner for temperature (big one)
        viewHolder.timeSpinner.setVisibility(View.GONE); // Spinner for time (little one)
        if (station == null) {
            viewHolder.stationTitle.setVisibility(View.GONE); // Label for station title
        } else {
            viewHolder.stationTitle.setVisibility(View.VISIBLE); // Label for station title
            viewHolder.stationTitle.setText(station.getNotes()); // Setting station title
        }
        viewHolder.errorLayout.setVisibility(View.VISIBLE); // Label for error
    }

    private void setTimeLabel(ViewHolder viewHolder, DataReading reading) {
        viewHolder.timeSpinner.setVisibility(View.GONE);
        viewHolder.timeLabel.setVisibility(View.VISIBLE);
        viewHolder.timeLabel.setText(formatDate(reading.getTimestamp()));
        viewHolder.timeLayout.setVisibility(View.VISIBLE);
        viewHolder.oldContent.setVisibility(isOldContent(reading.getTimestamp()) ? View.VISIBLE : View.GONE);
        // TODO Sane content
    }

    private void setTempLabel(ViewHolder viewHolder, DataReading reading) {
        viewHolder.tempSpinner.setVisibility(View.GONE);
        // TODO use temp converter
        viewHolder.tempLabel.setVisibility(View.VISIBLE);
        String temp = String.valueOf(reading.getAirReadings().getTemperature()) + SettingsManager.getTempUnit(activity.getApplicationContext());
        viewHolder.tempLabel.setText(temp);
    }

    /**
     * A boolean indicating whether the data is older than 30 minutes
     * @param date
     * @return True if the data is older than 30 minutes, otherwise false
     */
    private boolean isOldContent(Date date) {
        /*
         *  30 minutes = 30*60*1000 = 1.800.000 milliseconds
         *  System.currentTimeMillis compares to the time and date of the device (which maybe an emulator)
         *  - if so, the time may be off, if not set correctly in settings
         */
        Date threshold = new Date(System.currentTimeMillis()-1800000);
        return date.before(threshold);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().get(position).getId();
    }

    @Override
    public int getCount() {
        return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().size();
    }

    @Override
    public Object getItem(int position) {
        return DataRepositoryFactory.build(activity.getApplicationContext()).getStations().get(position);
    }

}

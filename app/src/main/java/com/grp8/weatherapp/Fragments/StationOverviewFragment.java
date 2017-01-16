package com.grp8.weatherapp.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Logic.SettingsManager;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.Logic.Converters.PressureConverter;
import com.grp8.weatherapp.Logic.Converters.TemperatureConverter;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static android.content.ContentValues.TAG;

public class StationOverviewFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private TextView temp, windSpeed, airP, humidity, updated;
    private TableLayout tableLayout;
    private ProgressBar spinner;
    private ImageView weatherWindow;
    private Handler handler;
    private TaskCanceler asyncCanceler;
    private boolean failed;

    private class DataRead extends AsyncTask<Void, DataReading, DataReading> {
        final int stationId = getActivity().getIntent().getExtras().getInt(Constants.KEY_STATION_ID);

        @Override
        protected DataReading doInBackground(Void... arg0) {
            try {
                return DataRepositoryFactory.build(getActivity().getApplicationContext()).getStationData(stationId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(DataReading reading) {
            if (reading != null) {
                updateView(reading);
            } else {
                failed = true;
                refresh(); }
        }
    }

    public class TaskCanceler implements Runnable {
        private AsyncTask task;

        public TaskCanceler(AsyncTask task) {
            this.task = task;
        }

        @Override
        public void run() {
            if (task.getStatus() == AsyncTask.Status.RUNNING) {
                task.cancel(true);
                failed = true;
                refresh();
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View   view;
        Intent intent = getActivity().getIntent();

        if(intent != null && ((intent.getBooleanExtra(Constants.KEY_STATION_NO_DATA, false)) || failed))
        {
            view = inflater.inflate(R.layout.fragment_station_overview_no_data, container, false);

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
            TextView icon = (TextView) view.findViewById(R.id.error_icon);

            icon.setTypeface(font);
        }
        else
        {
            view = inflater.inflate(R.layout.fragment_station_overview, container, false);

            // TextView declaration
            temp = (TextView) view.findViewById(R.id.temp);
            windSpeed = (TextView) view.findViewById(R.id.windSpeed);
            airP = (TextView) view.findViewById(R.id.airP);
            humidity = (TextView) view.findViewById(R.id.humidity);
            updated = (TextView) view.findViewById(R.id.updated);
            spinner = (ProgressBar) view.findViewById(R.id.spinner);

            // setting spinner visible and loading text
            loadedView();

            // initiates asynctask and cancels it after 15 sec if it hasn't finished
            loadData();

            // ImageView declaration
            weatherWindow = (ImageView) view.findViewById(R.id.weatherWindow);

            // TableLayout declaration
            tableLayout = (TableLayout) view.findViewById(R.id.tableLayoutt);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    private void updateView(DataReading reading) {

        // removing spinner
        spinner.setVisibility(View.GONE);

        // setting text

        // set temperature text and getting the appropriate unit
        String tem = String.valueOf(TemperatureConverter.getFormattedTemp(getActivity().getApplicationContext(), reading.getAirReadings().getTemperature()));
        temp.setText(tem + " " + SettingsManager.getTempUnit(getActivity().getApplicationContext()));

        // set windspeed text and getting the appropriate unit
        String speed = String.valueOf(reading.getWindReadings().getSpeed());
        windSpeed.setMaxWidth(315);
        windSpeed.setText(speed + " " + SettingsManager.getWindSpeedUnit(getActivity().getApplicationContext()));

        // set pressure text and getting the appropriate unit
        String pressure = String.valueOf(PressureConverter.getFormattedPressure(getActivity().getApplicationContext(), reading.getAirReadings().getPressure()));
        airP.setMaxWidth(315);
        airP.setText(pressure + " " + SettingsManager.getPressureUnit(getActivity().getApplicationContext()));
        if (reading.getAirReadings().getPressure() < 1000) {
            weatherWindow.setImageResource(R.mipmap.cloudy);
        } else {
            weatherWindow.setImageResource(R.mipmap.sunny);
        }

        // set humidity text and getting the appropriate unit
        String hum = String.valueOf(reading.getAirReadings().getHumidity());
        humidity.setText(hum + " %");

        // Set last updated text and getting the appropriate unit
        updated.setMaxWidth(315);
        updated.setText(String.valueOf(reading.getTimestamp()));
    }

    private void loadedView() {
        spinner.setVisibility(View.VISIBLE);
        temp.setText(R.string.loadingTextOverview);
        windSpeed.setText(R.string.loadingTextOverview);
        airP.setText(R.string.loadingTextOverview);
        humidity.setText(R.string.loadingTextOverview);
        updated.setText(R.string.loadingTextOverview);
    }

    private void loadData() {
        handler = new Handler();
        DataRead task = new DataRead();
        asyncCanceler = new TaskCanceler(task);
        handler.postDelayed(asyncCanceler, 15 * 1000);
        task.execute();
        if (asyncCanceler != null && handler != null) {
            handler.removeCallbacks(asyncCanceler);
        }
    }
    private void refresh() {
       Fragment stationOverviewFragment = getFragmentManager().findFragmentByTag("FRAGMENT");
       FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
       fragTransaction.detach(stationOverviewFragment);
       fragTransaction.attach(stationOverviewFragment);
       fragTransaction.commit();
    }

}

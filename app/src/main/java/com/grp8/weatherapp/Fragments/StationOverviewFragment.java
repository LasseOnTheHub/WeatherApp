package com.grp8.weatherapp.Fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.grp8.weatherapp.Data.DataRepository;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Model.SettingsManager;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.SupportingFiles.Constants;
import com.grp8.weatherapp.SupportingFiles.Converters.PressureConverter;
import com.grp8.weatherapp.SupportingFiles.Converters.TemperatureConverter;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StationOverviewFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    WeatherStations weatherStations;
    ArrayList<WeatherStation> weatherStationsArr;
    private TextView temp,windSpeed,airP, humidity, updated;
    private TableLayout tableLayout;
    DataRepository dataRepository;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View stationOverview = inflater.inflate(R.layout.fragment_station_overview, container, false);

        dataRepository = DataRepositoryFactory.build(getActivity().getApplicationContext());


        // TextView declaration
        temp = (TextView)stationOverview.findViewById(R.id.temp);
        windSpeed = (TextView)stationOverview.findViewById(R.id.windSpeed);
        airP = (TextView)stationOverview.findViewById(R.id.airP);
        humidity = (TextView)stationOverview.findViewById(R.id.humidity);
        updated = (TextView)stationOverview.findViewById(R.id.updated);

        // TableLayout declaration
        tableLayout = (TableLayout)stationOverview.findViewById(R.id.tableLayoutt);

        weatherStations = WeatherStations.getInstance();

        ArrayList<WeatherStation> stationer = weatherStations.getWeatherStations();

        Station station;
        int stationId = Integer.parseInt(getActivity().getIntent().getExtras().getString(Constants.KEY_USERID));
        DataReading reading;
        reading = dataRepository.getStationData(stationId);

        // setting text
        String tem = String.valueOf(TemperatureConverter.getFormattedTemp(getActivity().getApplicationContext(),reading.getAirReadings().getTemperature()));
        temp.setText(tem + " " + SettingsManager.getTempUnit(getActivity().getApplicationContext()));
        // temp.setText(String.valueOf(stationer.get(1).getWeatherData().getAirTemp()) + " \u2103");

        windSpeed.setText(String.valueOf(TemperatureConverter.getFormattedTemp(getActivity().getApplicationContext(),reading.getWindReadings().getSpeed())));
      //  windSpeed.setText(String.valueOf(stationer.get(1).getWeatherData().getWindSpeed())+" m/s");

        airP.setText(String.valueOf(PressureConverter.getFormattedPressure(getActivity().getApplicationContext(),reading.getAirReadings().getPressure())));
        //airP.setText(String.valueOf(stationer.get(1).getWeatherData().getAirPressure()) + " bar");


        humidity.setText(String.valueOf(stationer.get(1).getWeatherData().getAirHum())+" %");

        Date date = new Date(stationer.get(1).getWeatherData().getTimeStamp());
        SimpleDateFormat mdyFormat = new SimpleDateFormat("HH.mm");
        String mdy = mdyFormat.format(date);

        updated.setText(mdy);
        return stationOverview;
    }

    @Override
    public void onClick(View v) {
    }
}

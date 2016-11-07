package com.grp8.weatherapp.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StationOverviewActivity extends AppCompatActivity implements View.OnClickListener {

    WeatherStations weatherStations;
    ArrayList<WeatherStation> weatherStationsArr;
    private TextView temp,windSpeed,airP, humidity, updated;
    private TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_overview);

        // TextView declaration
        temp = (TextView)findViewById(R.id.temp);
        windSpeed = (TextView)findViewById(R.id.windSpeed);
        airP = (TextView)findViewById(R.id.airP);
        humidity = (TextView)findViewById(R.id.humidity);
        updated = (TextView)findViewById(R.id.updated);

        // TableLayout declaration
        tableLayout = (TableLayout)findViewById(R.id.tableLayoutt);

        tableLayout.setOnClickListener(this);

        weatherStations = WeatherStations.getInstance();

        ArrayList<WeatherStation> stationer = weatherStations.getWeatherStations();

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(stationer.get(1).getTitle());
        temp.setText(String.valueOf(stationer.get(1).getWeatherData().getAirTemp()) + " \u2103");
        windSpeed.setText(String.valueOf(stationer.get(1).getWeatherData().getWindSpeed())+" m/s");
        airP.setText(String.valueOf(stationer.get(1).getWeatherData().getAirPressure()) + " bar");
        humidity.setText(String.valueOf(stationer.get(1).getWeatherData().getAirHum())+" %");

        Date date = new Date(stationer.get(1).getWeatherData().getTimeStamp());
        SimpleDateFormat mdyFormat = new SimpleDateFormat("HH.mm");
        String mdy = mdyFormat.format(date);

        updated.setText(mdy);
    }

    public void onClick(View v) {
        if(v==tableLayout){
            Intent i = new Intent(this, StationDetailsActivity.class);
            startActivity(i);
        }

    }

   // weatherStations.getWeatherstation
}

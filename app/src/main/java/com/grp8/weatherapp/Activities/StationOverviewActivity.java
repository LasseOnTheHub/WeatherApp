package com.grp8.weatherapp.Activities;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.grp8.weatherapp.R;

public class StationOverviewActivity extends AppCompatActivity {

    private Button Details;
    private ImageView WeatherWindow, TempIcon, HumidityIcon, UpdateIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_overview);




    }


}

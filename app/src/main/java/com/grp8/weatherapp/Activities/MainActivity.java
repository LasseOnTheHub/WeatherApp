package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    WeatherStations weatherStations;

    /*
            Start MapOverviewActivity med denne.
                Intent intent = new Intent(MainActivity.this, MapOverviewActivity.class);
                startActivity(intent);
     */

    /*
            Hent vejrstationer med følgende.
            WeatherStations weatherStations;
            weatherStations = new WeatherStations();
            weatherStations.createWeatherStations();

            for (WeatherStation w: weatherStations.getWeatherStations())
                {
                }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        weatherStations = new WeatherStations();
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        String user = b.getString(Constants.KEY_USERID);
        // Frederik test


    }
}

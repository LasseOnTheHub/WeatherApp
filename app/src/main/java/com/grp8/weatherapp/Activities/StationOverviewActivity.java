package com.grp8.weatherapp.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.grp8.weatherapp.R;

public class StationOverviewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button DetailButton;
    // maybe ill just directly source the images in the XML file since they're gonna be static anyway
    private ImageView WeatherWindow, TempIcon, HumidityIcon, UpdateIcon;
    private TextView Temp, Humidity, Update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_overview);

        // TextView declaration
        Temp = (TextView)findViewById(R.id.Temp);
        Humidity = (TextView)findViewById(R.id.Humidity);
        Update = (TextView)findViewById(R.id.Update);

        // Button declaration
        DetailButton = (Button)findViewById(R.id.DetailButton);

        DetailButton.setOnClickListener(this);

    }

    public void onClick(View v) {
        if(v==DetailButton){
            Intent i = new Intent(this, StationDetailsActivity.class);
            startActivity(i);
        }

    }
}

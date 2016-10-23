package com.grp8.weatherapp.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.grp8.weatherapp.R;

public class StationOverviewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView Temp, Humidity, Updated;
    private TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_overview);

        // TextView declaration
        Temp = (TextView)findViewById(R.id.Temp);
        Humidity = (TextView)findViewById(R.id.Humidity);
        Updated = (TextView)findViewById(R.id.Updated);

        // TableLayout declaration
        tableLayout = (TableLayout)findViewById(R.id.tableLayoutt);

        tableLayout.setOnClickListener(this);

    }

    public void onClick(View v) {
        if(v==tableLayout){
            Intent i = new Intent(this, StationDetailsActivity.class);
            startActivity(i);
        }

    }
}

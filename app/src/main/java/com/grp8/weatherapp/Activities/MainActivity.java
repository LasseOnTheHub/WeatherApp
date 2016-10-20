package com.grp8.weatherapp.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.grp8.weatherapp.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        String user = b.getString(Constants.KEY_USERID);


        // RestService.getDevices(user);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Weather Stations");


        final String[] devices = {
                "Station 1","Station 2","Station 3","Station 4","Station 5",
                "Station 6","Station 7", "Station 8","Station 9","Station 10", "Station 11"
        };

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.stationlistelement, R.id.station_title, devices) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);
                TextView stationTitle = (TextView) view.findViewById(R.id.station_title);
                stationTitle.setText(devices[position]);

                return view;
            }
        };

        ListView list = (ListView) findViewById(R.id.stationslist);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setSelector(android.R.drawable.ic_notification_overlay);

    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        /*Intent intent = new Intent();
        // RestService.getDevices(user)[position];
        intent.putExtra(Constants.KEY_SELECTED_STATION, position);*/
        System.out.print("Pressed station " + position+1);
    }
}

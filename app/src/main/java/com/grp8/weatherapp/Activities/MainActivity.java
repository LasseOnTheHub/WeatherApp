package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.util.ArrayList;





public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WeatherStations weatherStations;

    /*
            Hent vejrstationer med følgende.
            WeatherStations weatherStations;
            weatherStations = new WeatherStations();
            weatherStations.createWeatherStations();

            for (WeatherStation w: weatherStations.getWeatherStations())
                {
                }

     */

    private FrameLayout searchLayout;
    private boolean searchIsVisible = false;
    private Button mapButton;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //For at hente test-data
        weatherStations = WeatherStations.getInstance();
        /*
            Kald weatherStations.getWeatherstation, som returnere et array af weatherstation.
         */

        setContentView(R.layout.activity_main);

        /*
         * Intent intent = getIntent();
         * Bundle b = intent.getExtras();
         * String user = b.getString(Constants.KEY_USERID);
         * RestService.getDevices(user);
         */

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Choose a station");

        // Dummy data
        // ###################################################################################

        final String[] devices = {
                "Station 1","Station 2","Station 3","Station 4","Station 5",
                "Station 6","Station 7", "Station 8","Station 9","Station 10", "Station 11"
        };

        // ###################################################################################

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.stationlistelement, R.id.station_title, devices)
        {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent)
            {
                View     view         = super.getView(position, cachedView, parent);
                TextView stationTitle = (TextView) view.findViewById(R.id.station_title);

                stationTitle.setText(devices[position]);

                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        startActivity(new Intent(MainActivity.this, StationOverviewActivity.class));
                    }
                });

                return view;
            }
        };

        ListView list = (ListView) findViewById(R.id.stationslist);
        list.setAdapter(adapter);

        searchLayout = (FrameLayout) findViewById(R.id.searchFrame);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(searchLayout.getWidth(), 0);
        searchLayout.setLayoutParams(lp);

        mapButton = (Button) findViewById(R.id.mapbutton);
        mapButton.setOnClickListener(this);
    }

    // Menu bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stationslist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                if (searchIsVisible) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(searchLayout.getWidth(), 0);
                    searchLayout.setLayoutParams(lp);
                } else {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(searchLayout.getWidth(), (int) getResources().getDimension(R.dimen.search_frame_height));
                    searchLayout.setLayoutParams(lp);
                }
                searchLayout.requestLayout();
                break;
            case R.id.add_station:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        if(v == this.mapButton)
        {
            Intent intent = new Intent(MainActivity.this, MapOverviewActivity.class);
            startActivity(intent);
        }
    }

}

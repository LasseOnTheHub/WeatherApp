package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStations;






public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout searchLayout;
    private boolean searchIsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /*
         * Intent intent = getIntent();
         * Bundle b = intent.getExtras();
         * String user = b.getString(Constants.KEY_USERID);
         * RestService.getDevices(user);
         */

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Choose a station");
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.stationlistelement, R.id.station_title, WeatherStations.getInstance().getWeatherStations())
        {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent)
            {
                View     view         = super.getView(position, cachedView, parent);
                TextView stationTitle = (TextView) view.findViewById(R.id.station_title);

                stationTitle.setText(WeatherStations.getInstance().getWeatherStations().get(position).getTitle());

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
            case R.id.settings_menu:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.map_menu:
                startActivity(new Intent(MainActivity.this, MapOverviewActivity.class));
                break;
            case R.id.search:       break;
            case R.id.add_station:  break;
            case R.id.refresh_menu: break;
            default:                break;
        }

        if (searchIsVisible) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(searchLayout.getWidth(), 0);
            searchLayout.setLayoutParams(lp);
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(searchLayout.getWidth(), (int) getResources().getDimension(R.dimen.search_frame_height));
            searchLayout.setLayoutParams(lp);
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {

    }

}

package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.Date;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FrameLayout searchLayout;
    private boolean searchIsVisible = true;
    private ListView list;

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

        WeatherStationAdapter adapter = new WeatherStationAdapter(this, R.layout.stationlistelement, WeatherStations.getInstance().getWeatherStations());

        list = (ListView) findViewById(R.id.stationslist);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        searchLayout = (FrameLayout) findViewById(R.id.searchFrame);

        shouldShowSearchBar();
        searchIsVisible = !searchIsVisible;
    }

    // Menu bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stationslist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        shouldShowSearchBar();

        switch (item.getItemId())
        {
            case R.id.settings_menu:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.map_menu:
                startActivity(new Intent(MainActivity.this, MapOverviewActivity.class));
                break;
            case R.id.search:
                searchIsVisible = !searchIsVisible;
                break;
            case R.id.add_station:  break;
            case R.id.refresh_menu: break;
            default:                break;
        }

        return true;
    }

    private void shouldShowSearchBar()
    {
        RelativeLayout.LayoutParams lp;

        if (searchIsVisible) {
            lp = new RelativeLayout.LayoutParams(searchLayout.getWidth(), 0);
        } else {
            lp = new RelativeLayout.LayoutParams(searchLayout.getWidth(), (int) getResources().getDimension(R.dimen.search_frame_height));
        }

        searchLayout.setLayoutParams(lp);
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id)
    {
        System.out.println("Pressed station " + position+1);
    }

}

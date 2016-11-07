package com.grp8.weatherapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStations;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FrameLayout searchLayout;
    private boolean searchIsVisible = false;
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
        searchLayout.setVisibility(RelativeLayout.GONE);
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
        if (item.getItemId() == R.id.search_menu) {
            toggleSearch();
        } else {
            hideSearchBar();
            switch (item.getItemId()) {
                case R.id.settings_menu:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;
                case R.id.map_menu:
                    startActivity(new Intent(MainActivity.this, MapOverviewActivity.class));
                    break;
                case R.id.add_station:
                    startActivity(new Intent(MainActivity.this, MainActivityTab.class));
                    break;
                case R.id.refresh_menu:
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void toggleSearch() {
        if (searchIsVisible) {
            hideSearchBar();
        } else {
            showSearchBar();
        }
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id)
    {
        hideSearchBar();
        startActivity(new Intent(MainActivity.this, StationOverviewActivity.class));
    }

    private void hideSearchBar() {
        if (searchIsVisible) {
            searchLayout.setVisibility(RelativeLayout.GONE);
            searchLayout.animate()
                    .translationY(-1 / 2 * searchLayout.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            EditText searchField = (EditText) findViewById(R.id.search);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                        }
                    });
        }
        searchIsVisible = !searchIsVisible;
    }

    private void showSearchBar() {
        if (!searchIsVisible) {
            searchLayout.setVisibility(RelativeLayout.VISIBLE);
            searchLayout.animate()
                    .translationY(1 / 2 * searchLayout.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            EditText searchField = (EditText) findViewById(R.id.search);
                            searchField.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
        }
        searchIsVisible = !searchIsVisible;
    }
}

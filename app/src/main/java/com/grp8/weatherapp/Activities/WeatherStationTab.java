package com.grp8.weatherapp.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.grp8.weatherapp.Fragments.GraphRainAndTemperatureFragment;
import com.grp8.weatherapp.Fragments.StationDetailsAirFragment;
import com.grp8.weatherapp.Fragments.StationDetailsRainFragment;
import com.grp8.weatherapp.Fragments.StationOverviewFragment;
import com.grp8.weatherapp.R;

public class WeatherStationTab extends AppCompatActivity
{
    private StationOverviewFragment stationOverviewFragment;

    private StationDetailsAirFragment         airFragment;
    private StationDetailsRainFragment        rainFragment;
    private GraphRainAndTemperatureFragment temperatureGraphFragment;
    private View datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Position",String.valueOf(position));
                Log.d("Position offset",String.valueOf(positionOffset));
                Log.d("PositionOffsetPixels",String.valueOf(positionOffsetPixels));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        datepicker = findViewById(R.id.datepicker);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Log.d("getting item",String.valueOf(position));
            switch(position)
            {
                case 0:
                    if (stationOverviewFragment == null) {
                        stationOverviewFragment = new StationOverviewFragment();
                    }
                    return stationOverviewFragment;
                case 1:
                    if (temperatureGraphFragment == null) {
                        temperatureGraphFragment = new GraphRainAndTemperatureFragment();
                    }
                    return temperatureGraphFragment;
                case 2:
                    if (airFragment == null) {
                        airFragment = new StationDetailsAirFragment();
                    }
                    return airFragment;
                case 3:
                    if (rainFragment == null) {
                        rainFragment = new StationDetailsRainFragment();
                    }
                    return rainFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0 :
                    return "Overview";
                case 1 :
                    return "Temp";
                case 2 :
                    return "Air";
                case 3 :
                    return "Rain";
                default:
                    return "?";
            }
        }
    }

    public void showDatePicker() {
        if (datepicker != null) {
            datepicker.setVisibility(View.VISIBLE);
        }
    }

    public void hideDatePicker() {
        if (datepicker != null) {
            datepicker.setVisibility(View.GONE);
        }
    }
}

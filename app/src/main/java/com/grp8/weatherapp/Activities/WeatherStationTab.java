package com.grp8.weatherapp.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.grp8.weatherapp.Fragments.GraphEarthAndAirMoist;
import com.grp8.weatherapp.Fragments.GraphRainAndTemperatureFragment;
import com.grp8.weatherapp.Fragments.StationDetailsAirFragment;
import com.grp8.weatherapp.Fragments.StationDetailsRainFragment;
import com.grp8.weatherapp.Fragments.StationDetailsTemperatureFragment;
import com.grp8.weatherapp.Fragments.StationOverviewFragment;
import com.grp8.weatherapp.R;

public class WeatherStationTab extends AppCompatActivity
{
    private StationOverviewFragment stationOverviewFragment;

    private GraphEarthAndAirMoist         airFragment;
    private StationDetailsRainFragment        rainFragment;
    private StationDetailsTemperatureFragment temperatureFragment;
    private GraphRainAndTemperatureFragment temperatureGraphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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
            switch(position)
            {
                case 0:
                    return stationOverviewFragment = new StationOverviewFragment();
                case 1:
                    return temperatureGraphFragment = new GraphRainAndTemperatureFragment();
                case 2:
                    return airFragment = new GraphEarthAndAirMoist();
                case 3:
                    return rainFragment = new StationDetailsRainFragment();
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
}

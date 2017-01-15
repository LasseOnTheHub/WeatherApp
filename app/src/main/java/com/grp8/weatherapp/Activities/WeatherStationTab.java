package com.grp8.weatherapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.MenuItem;
import android.os.Bundle;

import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Fragments.GraphTempRainHumidityFragment;
import com.grp8.weatherapp.Fragments.StationOverviewFragment;
import com.grp8.weatherapp.Fragments.DatePickerFragment;
import com.grp8.weatherapp.Fragments.GraphLuxPressure;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.R;

import java.util.Date;

public class WeatherStationTab extends AppCompatActivity
{
    private Date startDate = new Date(System.currentTimeMillis()-604800000);
    private Date endDate = new Date();

    private int stationID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_tab);

        this.stationID = getIntent().getExtras().getInt(Constants.KEY_STATION_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Station station = DataRepositoryFactory.build(getApplicationContext()).getStation(this.stationID);

        if(station != null)
        {
            toolbar.setTitle(station.getNotes());
        }

        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();

        if(supportActionBar != null)
        {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public int getCurrentStationID()
    {
        return this.stationID;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return true;
    }

    public void setFromDate(Date date)
    {
        this.startDate = date;

        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(1)).setFromDate(date);
        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(2)).setFromDate(date);
    }

    public void setToDate(Date date)
    {
        this.endDate = date;

        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(1)).setToDate(date);
        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(2)).setToDate(date);
    }

    public Date getStartDate()
    {
        return this.startDate;
    }

    public Date getEndDate()
    {
        return this.endDate;
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
            if(position == 0)
            {
                return new StationOverviewFragment();
            }
            else if(position == 1)
            {
                return new GraphTempRainHumidityFragment();
            }
            else
            {
                return new GraphLuxPressure();
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            int resource = R.string.station_tabs_unknown;

            switch(position)
            {
                case 0 :
                    resource = R.string.station_tabs_overview;
                    break;
                case 1 :
                    resource = R.string.station_tabs_temperature_rain;
                    break;
                case 2 :
                    resource = R.string.station_tabs_pressure_lux;
                    break;
            }

            return getString(resource);
        }
    }
}

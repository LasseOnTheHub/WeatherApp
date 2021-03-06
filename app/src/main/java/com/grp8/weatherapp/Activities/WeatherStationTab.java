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
import android.widget.Toast;

import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Fragments.GraphTempRainHumidityFragment;
import com.grp8.weatherapp.Fragments.StationOverviewFragment;
import com.grp8.weatherapp.Fragments.DatePickerFragment;
import com.grp8.weatherapp.Fragments.GraphWindPressure;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.R;

import java.util.Date;

public class WeatherStationTab extends AppCompatActivity
{
    private Date startDate = new Date(System.currentTimeMillis()-604800000);
    private Date endDate = new Date();

    private int stationID;
    private boolean shouldShowNoDataToastOnGraphAppearance = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_tab);

        this.stationID = getIntent().getExtras().getInt(Constants.KEY_STATION_ID);

        if (getIntent().getExtras().getBoolean(Constants.KEY_STATION_NO_DATA, false)) {
            shouldShowNoDataToastOnGraphAppearance = true;
        }

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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position != 0 && shouldShowNoDataToastOnGraphAppearance) {
                    Toast.makeText(getApplicationContext(), R.string.no_data, Toast.LENGTH_LONG).show();
                    shouldShowNoDataToastOnGraphAppearance = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

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
                return new GraphWindPressure();
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
                    resource = R.string.station_tabs_pressure_wind;
                    break;
            }

            return getString(resource);
        }
    }
}

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

import com.grp8.weatherapp.Fragments.DatePickerFragment;
import com.grp8.weatherapp.Fragments.GraphLuxPressure;
import com.grp8.weatherapp.Fragments.GraphTempRainHumidityFragment;
import com.grp8.weatherapp.Fragments.StationOverviewFragment;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.R;

import java.util.Date;

public class WeatherStationTab extends AppCompatActivity
{

    private Date startDate = new Date(System.currentTimeMillis()-604800000);
    private Date endDate = new Date();
    public int stationId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        stationId = getIntent().getExtras().getInt(Constants.KEY_STATION_ID);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
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

    public void setFromDate(Date date) {
        startDate = date;
        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(1)).setFromDate(date);
        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(2)).setFromDate(date);
    }

    public void setToDate(Date date) {
        endDate = date;

        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(1)).setToDate(date);
        ((DatePickerFragment)getSupportFragmentManager().getFragments().get(2)).setToDate(date);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
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
            if (position == 0) {
                return new StationOverviewFragment();
            } else if (position == 1) {
                return new GraphTempRainHumidityFragment();
            } else {
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
            switch (position)
            {
                case 0 :
                    return "Overview";
                case 1 :
                    return "Temp+Rain";
                case 2 :
                    return "Pressure+Lux";
                default:
                    return "";
            }
        }
    }
}

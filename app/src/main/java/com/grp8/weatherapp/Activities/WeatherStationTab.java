package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.grp8.weatherapp.R;

public class WeatherStationTab extends AppCompatActivity {

    private StationOverviewFragment stationOverviewFragment;
    private Graf1 graf1;   //Omdøb til de faktiske navne af fragmenterne
    private Graf2 graf2;   //Omdøb til de faktiske navne af fragmenterne
    private Graf3 graf3;   //Omdøb til de faktiske navne af fragmenterne

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stationslist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu:
                Log.d("Refresh","...");
                break;
            case R.id.settings_menu:
                startActivity(new Intent(WeatherStationTab.this, SettingsActivity2.class));
                break;
            case android.R.id.home:
                finish();
                break;
            default: break;
        }
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (stationOverviewFragment == null) {
                    stationOverviewFragment = new StationOverviewFragment();
                }
                return stationOverviewFragment;
            } else if(position ==1) {
                if (graf1 == null) {
                    graf1 = new Graf1();
                }
                return graf1;
            } else if(position ==2) {
                if(graf2 ==null){
                    graf2 = new Graf2();
            }
                return graf2;
            }
            else{
                if(graf3 ==null){
                    graf3 = new Graf3();
                }
                return graf3;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0 : return "Oversigt";
                case 1 : return "Graf";
                case 2 : return "Graf";
                case 3 : return "Graf";
                default:return "?";
            }
        }
    }
}

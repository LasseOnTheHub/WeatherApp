package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.grp8.weatherapp.R;

import com.grp8.weatherapp.Fragments.MainFragment;
import com.grp8.weatherapp.Fragments.MapViewFragment;

public class MainActivityTab extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MainFragment mainFrag;
    private MapViewFragment mapViewFragment;
    private static final int TIME_INTERVAL = 3000; // // milliseconds, time passed between two back presses.
    private long backPressed;
    private Toast exitToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        setupActionBar();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    if (mainFrag.isSearchVisible()) {
                        mainFrag.toggleSearch(true);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setTitle(R.string.title_mainActivity);
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
                mainFrag.load();
                break;
            case R.id.settings_menu:
                startActivity(new Intent(MainActivityTab.this, SettingsActivity.class));
                break;
            case R.id.search_menu:
                mViewPager.setCurrentItem(0);
                ((MainFragment) mSectionsPagerAdapter.getItem(0)).toggleSearch(true);
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
                if (mainFrag == null) {
                    mainFrag = new MainFragment();
                }
                return mainFrag;
            } else {
                if (mapViewFragment == null) {
                    mapViewFragment = new MapViewFragment();
                }
                return mapViewFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? getString(R.string.ListTab) : getString(R.string.MapTab);
        }
    }

    @Override
    public void onBackPressed()
    {
        exitToast = Toast.makeText(getApplicationContext(), R.string.toast_exit, Toast.LENGTH_SHORT);
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { exitToast.show(); }

        backPressed = System.currentTimeMillis();
    }

}

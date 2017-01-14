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

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.grp8.weatherapp.R;

import com.grp8.weatherapp.Fragments.MainFragment;
import com.grp8.weatherapp.Fragments.MapViewFragment;

public class MainActivityTab extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final int TIME_INTERVAL = 3000; // // milliseconds, time passed between two back presses.
    private long backPressed;

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

                    if (getMainFragment().isSearchVisible()) {
                        getMainFragment().toggleSearch(true);
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
                getMainFragment().load();
                // getMapFragment.update();
                break;
            case R.id.settings_menu:
                startActivity(new Intent(MainActivityTab.this, SettingsActivity.class));
                break;
            case R.id.search_menu:
                mViewPager.setCurrentItem(0);
                getMainFragment().toggleSearch(true);
                break;
            case R.id.logout_menu:
                //TODO: Add logout logic
                break;
            default: break;
        }
        return true;
    }

    private MainFragment getMainFragment() {
        return (MainFragment) getSupportFragmentManager().getFragments().get(0);
    }

    private MapViewFragment getMapFragment() { // FIXME: Remove?
        return (MapViewFragment) getSupportFragmentManager().getFragments().get(1);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new MainFragment() : new MapViewFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? getString(R.string.ListTab) : getString(R.string.MapTab); // TODO: Add language strings
        }
    }


    @Override
    public void onBackPressed()
{
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return;
        }
        else { Toast.makeText(getApplicationContext(), R.string.toast_exit, Toast.LENGTH_SHORT).show(); }
        backPressed = System.currentTimeMillis();
    }

}

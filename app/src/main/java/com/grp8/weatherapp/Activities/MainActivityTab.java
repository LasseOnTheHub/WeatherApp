package com.grp8.weatherapp.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //searchView.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                getMainFragment().toggleSearch();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getMainFragment().toggleSearch();
                return true;
            }
        });
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
            case R.id.action_search:

                break;
            case R.id.logout_menu:
                //TODO
                break;
            default: break;
        }
        return true;
    }

    private MainFragment getMainFragment() {
        return (MainFragment) getSupportFragmentManager().getFragments().get(0);
    }

    private MapViewFragment getMapFragment() {
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
            return position == 0 ? getString(R.string.ListTab) : getString(R.string.MapTab);
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

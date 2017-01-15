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
import com.google.android.gms.maps.SupportMapFragment;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Logic.UserManager;
import com.grp8.weatherapp.R;

import com.grp8.weatherapp.Fragments.MainFragment;
import com.grp8.weatherapp.Fragments.MapViewFragment;

public class MainActivityTab extends AppCompatActivity
{
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if(intent != null && intent.getExtras().getBoolean("reload", false))
        {
            this.refresh();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stationslist, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewPager.setCurrentItem(0, true);
                getMainFragment().getListAdapter().getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getMainFragment().getListAdapter().getFilter().filter(newText);
                return true;
            }
        });
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
                this.refresh();
                break;
            case R.id.settings_menu:
                startActivityForResult(new Intent(MainActivityTab.this, SettingsActivity.class), 1);
                break;
            case R.id.logout_menu:
                UserManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(MainActivityTab.this, LogonActivity.class));
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh()
    {
        Log.d("Refresh","...");
        DataRepositoryFactory.build(getApplicationContext()).refresh();
        getMainFragment().load();
        // getMapFragment.update();
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
            return position == 0 ? getString(R.string.listTab) : getString(R.string.mapTab);
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

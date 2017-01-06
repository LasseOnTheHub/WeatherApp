package com.grp8.weatherapp.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.grp8.weatherapp.R;

/**
 * Created by Frederik on 06/01/2017.
 */

public class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_weatherapp);
            // setupActionBar();
            // setHasOptionsMenu(true); ??
            /*
            bindPreferenceSummaryToValue(findPreference("temp_unit"));
            bindPreferenceSummaryToValue(findPreference("pressure_unit"));
            bindPreferenceSummaryToValue(findPreference("windspeed_unit"));
            bindPreferenceSummaryToValue(findPreference("app_language"));
            */
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            getActivity().finish();
            return true;
        }

    /*private void setupActionBar() {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Settings");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }*/
    }

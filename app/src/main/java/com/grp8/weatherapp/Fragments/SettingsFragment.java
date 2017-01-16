package com.grp8.weatherapp.Fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.grp8.weatherapp.Activities.SettingsActivity;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.Logic.Constants;

/**
 * Created by Frederik on 06/01/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    private boolean ready = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_weatherapp);

        bindPreferenceSummaryToValue(findPreference(Constants.KEY_TEMP_UNIT));
        bindPreferenceSummaryToValue(findPreference(Constants.KEY_PRESS_UNIT));
        bindPreferenceSummaryToValue(findPreference(Constants.KEY_WS_UNIT));
        bindPreferenceSummaryToValue(findPreference(Constants.KEY_LANG));

        Preference button = findPreference(getString(R.string.app_logout));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO
                return true;
            }
        });

        this.ready = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().finish();
        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

            if(ready)
            {
                ((SettingsActivity) getActivity()).setChangedStatus(true);
            }

            return true;
        }
    };


}
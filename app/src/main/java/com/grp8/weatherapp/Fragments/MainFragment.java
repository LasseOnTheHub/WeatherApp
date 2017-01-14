package com.grp8.weatherapp.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.grp8.weatherapp.Activities.WeatherStationTab;
import com.grp8.weatherapp.Adapters.WeatherStationsAdapter;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.R;

import com.grp8.weatherapp.SupportingFiles.Constants;
import com.grp8.weatherapp.SupportingFiles.Utils;
import com.grp8.weatherapp.TestData.WeatherStation;

import com.grp8.weatherapp.Logic.Constants;


import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static android.view.View.VISIBLE;

/**
 * Created by Frederik on 14/11/2016.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView list;
    private TextView spinnerText;

    private RelativeLayout spinnerFrame; // Background spinner

    private class LoadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                DataRepositoryFactory.build(getActivity().getApplicationContext()).getStations();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateList();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainFrag = inflater.inflate(R.layout.fragment_stationlist, container, false);
        spinnerFrame = (RelativeLayout) mainFrag.findViewById(R.id.spinner_layout);
        spinnerText = (TextView) mainFrag.findViewById(R.id.spinner_text);
        list = (ListView) mainFrag.findViewById(R.id.stationlist);
        new LoadTask().execute();
        list.setOnItemClickListener(this);

        return mainFrag;
    }

    public void load() {

        if (list == null) {
            list = (ListView) getView().findViewById(R.id.stationlist);
            list.setOnItemClickListener(this);
        }

        if (list.getVisibility() == View.VISIBLE) {
            list.setVisibility(View.GONE);
            spinnerFrame.setVisibility(RelativeLayout.VISIBLE);
            spinnerText.setText(R.string.loadingText);
            new LoadTask().execute();
        }
    }

    private void updateList() {
        spinnerFrame.setVisibility(RelativeLayout.GONE);
        list.setVisibility(View.VISIBLE);
        list.setAdapter(new WeatherStationsAdapter(getActivity(), list));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getActivity(), WeatherStationTab.class).putExtra(Constants.KEY_STATION_ID, (int) id);

        startActivity(intent);
    }

    public void toggleSearch() {
        ((WeatherStationsAdapter) list.getAdapter()).toggleSearch();
    }

    public WeatherStationsAdapter getListAdapter() {
        return (WeatherStationsAdapter) list.getAdapter();
    }

}

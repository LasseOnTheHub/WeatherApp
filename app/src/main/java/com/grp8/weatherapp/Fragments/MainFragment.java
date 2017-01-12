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
import com.grp8.weatherapp.Logic.Constants;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static android.view.View.VISIBLE;

/**
 * Created by Frederik on 14/11/2016.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView list;
    private TextView spinnerText;

    private FrameLayout searchFrame;
    private RelativeLayout spinnerFrame;

    private boolean searchIsVisible = false;

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
        searchFrame = (FrameLayout) mainFrag.findViewById(R.id.searchFrame);
        searchFrame.setVisibility(FrameLayout.GONE);
        list = (ListView) mainFrag.findViewById(R.id.stationlist);
        DataRepositoryFactory.build(getActivity().getApplicationContext()).setUser(5);
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

    @Override
    public void onPause() {
        super.onPause();
        if (searchIsVisible) {
            toggleSearch(true);
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

    public void toggleSearch(boolean shouldChange) {
        if (searchIsVisible) {
            searchFrame.animate()
                    .translationY(-1/2*searchFrame.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            EditText searchField = (EditText) getView().findViewById(R.id.searchField);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                         }
                    });
            searchFrame.setVisibility(FrameLayout.GONE);
        } else {
            searchFrame.setVisibility(VISIBLE);
            searchFrame.animate()
                    .translationY(1/2*searchFrame.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            EditText searchField = (EditText) getView().findViewById(R.id.searchField);
                            searchField.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
        }

        if (shouldChange) {
            searchIsVisible = !searchIsVisible;
        }
    }

    public boolean isSearchVisible() {
        return searchIsVisible;
    }

}

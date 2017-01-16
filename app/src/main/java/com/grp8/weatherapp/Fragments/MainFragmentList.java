package com.grp8.weatherapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grp8.weatherapp.Activities.WeatherStationTab;
import com.grp8.weatherapp.Adapters.WeatherStationAdapter;
import com.grp8.weatherapp.Adapters.WeatherStationsAdapter;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Frederik on 16/01/2017.
 */

public class MainFragmentList extends ListFragment implements Filterable {

    private TextView            backgroundText;
    private boolean             isSearching = false;
    private List<Station>       stations;
    private List<DataReading>   readings;

    // List backgrounds
    private LinearLayout backgroundViewNoResults;
    private RelativeLayout backgroundViewLoading;

    public MainFragmentList() {
        super();
        setRetainInstance(true);
        // Fragment doesn't get recreated
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stationlist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        backgroundViewLoading   =   (RelativeLayout) view.findViewById(R.id.spinner_layout);
        backgroundViewNoResults =   (LinearLayout) view.findViewById(R.id.no_results);
        backgroundText          =   (TextView) view.findViewById(R.id.no_results_text);
        backgroundViewLoading.setVisibility(View.GONE);
        backgroundViewNoResults.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            // Orientation change or app getting resumed
            // stations = Cached stations from DataRepository
            // readings = Cached readings from DataRepository
            getListView().setEmptyView(backgroundViewNoResults);
            backgroundViewNoResults.setVisibility(View.VISIBLE);
        } else {
            // App runs for the first time
            resetData();
            getListView().setEmptyView(backgroundViewLoading);
            backgroundViewLoading.setVisibility(View.VISIBLE);
            new LoadTask().execute();
        }
        setAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Saving state
        outState.putBoolean("isSearching", isSearching);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(getActivity(), WeatherStationTab.class).putExtra(Constants.KEY_STATION_ID, (int) id));
    }

    public void load() {
        resetData();
        setAdapter();
        backgroundViewNoResults.setVisibility(View.GONE);
        backgroundViewLoading.setVisibility(View.VISIBLE);
        getListView().setEmptyView(backgroundViewLoading);
        new LoadTask().execute();
    }

    private void loadingComplete() {
        // Setting background to no results
        backgroundViewLoading.setVisibility(View.GONE);
        getListView().setEmptyView(backgroundViewNoResults);
        backgroundViewNoResults.setVisibility(View.VISIBLE);
        setAdapter();
    }

    private void resetData() {
        stations = new ArrayList<Station>();
        readings = new ArrayList<DataReading>();
        setAdapter();
    }

    public void toggleSearch() {
        isSearching = !isSearching;
        // Adjusting background
        backgroundViewLoading.setVisibility(View.GONE);
        getListView().setEmptyView(backgroundViewNoResults);
        String text = isSearching ? getString(R.string.no_search_results) : getString(R.string.no_stations);
        backgroundText.setText(text);
        setAdapter();
    }

    private void setAdapter() {
        getListView().setAdapter(new WeatherStationAdapter(stations, readings, getLayoutInflater(null), getActivity().getApplicationContext()));
    }

    private void setAdapter(List<Station> stations, List<DataReading> readings) {
        getListView().setAdapter(new WeatherStationAdapter(stations, readings, getLayoutInflater(null), getActivity().getApplicationContext()));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Station> stations = new ArrayList<>();
                List<DataReading> readings = new ArrayList<>();
                Map<List<Station>, List<DataReading>> map = new HashMap<>();

                if (constraint != null && constraint.length() != 0) {
                    for(int i = 0; i<MainFragmentList.this.stations.size(); i++) {
                        Station station = MainFragmentList.this.stations.get(i);
                        if (station.getNotes().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            stations.add(station);
                            readings.add(MainFragmentList.this.readings.get(i));
                        }
                    }
                } else {
                    stations = MainFragmentList.this.stations;
                    readings = MainFragmentList.this.readings;
                }
                map.put(stations, readings);
                FilterResults results = new FilterResults();
                results.values = map;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Map<List<Station>, List<DataReading>> map = (Map<List<Station>, List<DataReading>>) results.values;
                Map.Entry<List<Station>,List<DataReading>> entry = map.entrySet().iterator().next();
                MainFragmentList.this.setAdapter(entry.getKey(), entry.getValue());
            }
        };
    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                stations = DataRepositoryFactory.build(getActivity().getApplicationContext()).getStations();
                for (int i = 0; i < stations.size(); i++) {
                    readings.add(DataRepositoryFactory.build(getActivity().getApplicationContext()).getStationData(stations.get(i).getId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loadingComplete();
        }
    }

}

package com.grp8.weatherapp.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.grp8.weatherapp.Activities.WeatherStationTab;
import com.grp8.weatherapp.Adapters.WeatherStationsAdapter;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.Logic.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Frederik on 14/11/2016.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView list;
    private TextView spinnerText, backgroundText;

    private RelativeLayout spinnerFrame; // Background spinner

    private static List<Integer> nodata = new ArrayList<>();

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
        backgroundText = (TextView) mainFrag.findViewById(R.id.no_results_text);
        list.setEmptyView(mainFrag.findViewById(R.id.no_results));
        new LoadTask().execute();
        list.setOnItemClickListener(this);

        return mainFrag;
    }

    public static void markStationAsOutdated(int station)
    {
        nodata.add(station);
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

        /*
         * This method is called when refresh is called and should therefore also
         * clear any stations marked as containing no recent data.
         */
        nodata.clear();
    }

    private void updateList() {
        spinnerFrame.setVisibility(RelativeLayout.GONE);
        list.setVisibility(View.VISIBLE);
        list.setAdapter(new WeatherStationsAdapter(getActivity(), list));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getActivity(), WeatherStationTab.class);

        System.out.println(nodata);
        System.out.println(nodata.contains((int) id));

        intent.putExtra(Constants.KEY_STATION_ID, (int) id);
        intent.putExtra(Constants.KEY_STATION_NO_DATA, nodata.contains((int) id));

        startActivity(intent);
    }

    public void toggleSearch() {
        getListAdapter().toggleSearch();
        String text = getListAdapter().isSearching() ? getString(R.string.no_search_results) : getString(R.string.no_stations);
        backgroundText.setText(text);

    }

    public WeatherStationsAdapter getListAdapter() {
        return (WeatherStationsAdapter) list.getAdapter();
    }

}

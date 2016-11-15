package com.grp8.weatherapp.Activities;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grp8.weatherapp.R;

/**
 * Created by Frederik on 14/11/2016.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView list;
    private FrameLayout searchFrame;
    private boolean searchIsVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainFrag = inflater.inflate(R.layout.fragment_stationlist, container, false);

        list = (ListView) mainFrag.findViewById(R.id.stationlist);
        list.setAdapter(new WeatherStationsAdapter(getActivity()));
        list.setOnItemClickListener(this);

        searchFrame = (FrameLayout) mainFrag.findViewById(R.id.searchFrame);
        searchFrame.setVisibility(FrameLayout.GONE);
        searchIsVisible = false;

        return mainFrag;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Clicked on item",String.valueOf(position));
    }

    public void toggleSearch(boolean shouldChange) {
        if (searchIsVisible) {
            searchFrame.setVisibility(FrameLayout.GONE);
        } else {
            searchFrame.setVisibility(FrameLayout.VISIBLE);
        }

        if (shouldChange) {
            searchIsVisible = !searchIsVisible;
        }
    }
}

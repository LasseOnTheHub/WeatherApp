package com.grp8.weatherapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grp8.weatherapp.R;

public class StationDetailsSoilFragment extends Fragment
{
    public StationDetailsSoilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_station_details_soil, container, false);
    }
}

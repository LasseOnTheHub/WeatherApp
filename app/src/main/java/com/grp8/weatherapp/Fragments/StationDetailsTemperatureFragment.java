package com.grp8.weatherapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grp8.weatherapp.R;

public class StationDetailsTemperatureFragment extends Fragment
{
    public StationDetailsTemperatureFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_station_details_temperature, container, false);
    }
}

package com.grp8.weatherapp.Model.Deprecated;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Henrik on 03-01-2017.
 */
public class Station {

    private int id;
    private LatLng location;

    public int getId() {
        return id;
    }

    public LatLng getLocation() {
        return location;
    }
}

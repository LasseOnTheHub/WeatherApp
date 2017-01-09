package com.grp8.weatherapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grp8.weatherapp.Data.DataRepository;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.SupportingFiles.Constants;
import com.grp8.weatherapp.Activities.StationOverviewActivity;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by lasse on 11/21/16.
 */

public class MapViewFragment extends android.support.v4.app.Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    DataRepository dataRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_overview, container, false);
        
        dataRepository = DataRepositoryFactory.build(getActivity().getApplicationContext());
        dataRepository.setUser(5);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;


                //TODO: Her skal der implementeres således at der spørges efter tilladelse til at benytte GPS i Runtime.
                // For showing a move to my location button
                //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //  return;
                //}
                //googleMap.setMyLocationEnabled(true);
try {
    new AsyncTask() {
        @Override
        protected Object doInBackground(Object... arg0) {
            try {
                return dataRepository.getStations();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object s) {
            if (s == null)
                return;
            ArrayList<Marker> markers = new ArrayList<>();
            for (final Station w : (List<Station>) s) {
                LatLng latLng = new LatLng(w.getLatitude(), w.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(w.getNotes()));
                markers.add(marker);

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        //Toast.makeText(getApplicationContext(), "Marker Pushed",  Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), StationOverviewActivity.class);
                        intent.putExtra(Constants.KEY_USERID, w.getId());
                        startActivity(intent);
                    }
                });
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();

            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            //googleMap.animateCamera(cu);
            googleMap.moveCamera(cu);
        }
    }.execute();
}
catch (Exception e)
{
    e.printStackTrace();
}

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}


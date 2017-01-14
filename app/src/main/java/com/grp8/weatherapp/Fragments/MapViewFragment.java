package com.grp8.weatherapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Data.IDataRepository;
import com.grp8.weatherapp.Entities.Data.Air;
import com.grp8.weatherapp.Entities.Data.Soil;
import com.grp8.weatherapp.Entities.Data.Wind;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Activities.WeatherStationTab;

import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.Logic.Converters.TemperatureConverter;
import com.grp8.weatherapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
 * Created by lasse on 11/21/16.
 */

public class MapViewFragment extends android.support.v4.app.Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    MapView mMapView;
    private GoogleMap googleMap;
    IDataRepository dataRepository;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    List<Station> stations;
    List<DataReading> dataReadings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_overview, container, false);

        dataRepository = DataRepositoryFactory.build(getActivity().getApplicationContext());

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                try {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... args) {
                            try {
                                stations = dataRepository.getStations();
                                for (Station s :stations)
                                {
                                    dataReadings.add(dataRepository.getStationData(s.getId()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void v) {
                            if (stations == null) {
                                return;
                            }
                            getDeviceLocation();
                            List<Marker> markers = new ArrayList<>();
                            dataRepository.setUser(5);
                            for (final Station s : stations) {
                                DataReading temporaryDataReading = new DataReading(0,0,new Date(),0,new Air(0,0,0),new Wind(0,0),new Soil(new int[0],new int[0]));
                                if(dataReadings!=null)
                                for (DataReading d: dataReadings) {
                                    if (d.getID() ==s.getId()){
                                        temporaryDataReading = d;
                                    }
                                }
                                String tem = String.valueOf(TemperatureConverter.getFormattedTemp(getActivity().getApplicationContext(),temporaryDataReading.getAirReadings().getTemperature()));
                                int hum = temporaryDataReading.getAirReadings().getPressure();
                                LatLng latLng = new LatLng(s.getLatitude(), s.getLongitude());
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(s.getNotes()).snippet("Temperatur:"+tem+", Fugtighed:"+hum));

                                markers.add(marker);

                                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Intent intent = new Intent(getActivity(), WeatherStationTab.class);
                                        intent.putExtra(Constants.KEY_STATION_ID, s.getId());

                                        startActivity(intent);
                                    }
                                });
                            }

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            for (Marker marker : markers) {
                                builder.include(marker.getPosition());
                            }
                            LatLngBounds bounds = builder.build();
                            int padding = 200;
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            googleMap.moveCamera(cu);
                        }
                    }.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                googleMap.setMyLocationEnabled(true);
            }
            catch (SecurityException se)
            {
                se.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        googleMap.setMyLocationEnabled(true);
                    }
                    catch (SecurityException se)
                    {
                        se.printStackTrace();
                    }
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}


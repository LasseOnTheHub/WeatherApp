package com.grp8.weatherapp.Fragments;

import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.util.Log;
import android.widget.Toast;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.grp8.weatherapp.Activities.WeatherStationTab;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Data.IDataRepository;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;

import com.grp8.weatherapp.Logic.Converters.TemperatureConverter;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by lasse on 11/21/16.
 */

public class MapViewFragment extends android.support.v4.app.Fragment implements ActivityCompat.OnRequestPermissionsResultCallback
{
    private final static String TAG = "MapView";
    private final static int    MAP_PADDING = 200;

    private MapView   mMapView;
    private GoogleMap googleMap;
    private IDataRepository repository;

    private Map<String, Integer> markerIDs = new HashMap<>();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_map_overview, container, false);

        repository = DataRepositoryFactory.build(getActivity().getApplicationContext());

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(final GoogleMap map)
            {
                googleMap = map;

                try
                {
                    new AsyncTask<Void, Map<Station, DataReading>, Map<Station, DataReading>>()
                    {
                        @Override
                        protected Map<Station, DataReading> doInBackground(Void... args)
                        {
                            Map<Station, DataReading> results = new HashMap<>();

                            try
                            {
                                List<Station> stations = repository.getStations();

                                for(Station station : stations)
                                {
                                    DataReading reading = repository.getStationData(station.getId());

                                    results.put(station, reading);
                                }
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                                return null;
                            }

                            return results;
                        }

                        @Override
                        protected void onPostExecute(Map<Station, DataReading> stations)
                        {
                            if(stations == null)
                            {
                                return;
                            }

                            getDeviceLocation();

                            List<Marker> markers = new ArrayList<>();

                            for(final Map.Entry<Station, DataReading> entry : stations.entrySet())
                            {
                                final Station station = entry.getKey();
                                DataReading   reading = entry.getValue();

                                Log.d("MAP", "Stations: " + station.getId() + " - " + station.getNotes());

                                String snippet;

                                if(reading != null)
                                {
                                    String tem = String.valueOf(TemperatureConverter.getFormattedTemp(getActivity().getApplicationContext(),reading.getAirReadings().getTemperature()));

                                    int hum = reading.getAirReadings().getHumidity();

                                    snippet = getString(R.string.map_snippet_temperature) + ": " + tem + ", " + getString(R.string.map_snippet_humidity) +": " + hum + "%";
                                }
                                else
                                {
                                    snippet = getString(R.string.map_snippet_no_data);
                                }

                                LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
                                Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(station.getNotes()).snippet(snippet));

                                markers.add(marker);

                                /*
                                 * Associated auto-generated map ID with station ID.
                                 */
                                markerIDs.put(marker.getId(), station.getId());

                                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                                {
                                    @Override
                                    public void onInfoWindowClick(Marker marker)
                                    {
                                        if(!markerIDs.containsKey(marker.getId()))
                                        {
                                            Log.e(TAG, "Failed to find station ID associated with marker: " + marker.getId());

                                            Toast.makeText(getActivity(), R.string.map_snippet_load_failed, Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        Intent intent = new Intent(getActivity(), WeatherStationTab.class).putExtra(Constants.KEY_STATION_ID, markerIDs.get(marker.getId()));
                                        startActivity(intent);
                                    }
                                });
                            }

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            for(Marker marker : markers)
                            {
                                builder.include(marker.getPosition());
                            }

                            if(!markers.isEmpty())
                            {
                                map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING));
                            }
                        }
                    }.execute();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    private void getDeviceLocation()
    {
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            try
            {
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
            }
            catch(SecurityException se)
            {
                se.printStackTrace();
            }
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            try
            {
                googleMap.setMyLocationEnabled(true);
            }
            catch(SecurityException e)
            {
                e.printStackTrace();
            }
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
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


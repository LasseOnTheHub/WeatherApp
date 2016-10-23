package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.util.ArrayList;

public class MapOverviewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    WeatherStations weatherStations;
    ArrayList<WeatherStation> weatherStationsArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_overview);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        weatherStations = WeatherStations.getInstance();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<Marker> markers = new ArrayList<>();

        for (final WeatherStation w: weatherStations.getWeatherStations())
        {
            LatLng latLng = new LatLng(w.getLatitude(), w.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(w.getTitle()));
            markers.add(marker);

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    Toast.makeText(getApplicationContext(), "Marker Pushed",  Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapOverviewActivity.this, StationOverviewActivity.class);
                    intent.putExtra(Constants.KEY_USERID,w.getID());
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
        googleMap.animateCamera(cu);
    }
}

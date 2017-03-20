package com.example.shann.galleriesofjustice;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    SharedPreferences preferences;

    private Button btnLocate;
    private  Button btnMuseum;

    private GoogleMap mMap;

    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    private Marker marker;

    final double MuseumLat = 52.9508283;
    final double MuseumLng = -1.144286;
    final int MuseumZoom = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);
        preferences.edit().putBoolean("Tour Guide", true).apply();

        btnLocate = (Button) findViewById(R.id.btnLocate);
        btnMuseum = (Button) findViewById(R.id.btnMuseum);

        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        btnMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation(MuseumLat, MuseumLng, MuseumZoom);
            }
        });
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

        goToLocation(MuseumLat, MuseumLng, MuseumZoom);
        

        //TODO: Set beacon locations, pins
    }

    private void goToLocation(double lat, double lng, int zoom) {

        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);

        setMarker(mLocation.toString(), lat, lng, BitmapDescriptorFactory.fromResource(R.mipmap.ic_beacon_marker));
    }

    private void getCurrentLocation() {

        LatLng here = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(here).title("You Are Here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));

        setMarker(mLocation.toString(), latitude, longitude, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
    }

    private void setMarker(String location, double lat, double lng, BitmapDescriptor bitmapDescriptor) {

        if (marker != null) {
            marker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .title(mLocation.toString())
                .icon(bitmapDescriptor)
                .position(new LatLng(lat, lng))
                .snippet("You Are Here!");
        marker = mMap.addMarker(markerOptions);
    }
}
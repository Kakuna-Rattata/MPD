package com.example.shann.galleriesofjustice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    SharedPreferences preferences;

    private Button btnLocate;
    private  Button btnMuseum;

    private GoogleMap mMap;

    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;

    private Marker museumMarker;
    private Marker locatiomMarker;

    final double MuseumLat = 52.9508283;
    final double MuseumLng = -1.144286;
    final int MuseumZoom = 12;

    private BitmapDescriptor museumIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);

        if (preferences.getBoolean(getString(R.string.achievements_tourguide), false) == false) {
            preferences.edit().putBoolean(getString(R.string.achievements_tourguide), true).apply();
            Intent achievementIntent = new Intent(getApplicationContext(), AchievementsActivity.class);
            achievementIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GlobalClass.showNotification(
                    getString(R.string.achievement_unlocked) + ": " + getString(R.string.achievements_tourguide),
                    getString(R.string.achievements_tourguide_criteria),
                    achievementIntent,
                    getApplicationContext(),
                    GlobalClass.NOTIFICATION_ACHIEVEMENT);
        }

        btnLocate = (Button) findViewById(R.id.btnLocate);
        btnMuseum = (Button) findViewById(R.id.btnMuseum);

        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();

        museumIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_museum_pin);

        //  Prevents crash when location not available :
        try {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        } catch(Exception ex) {
            finish();
        }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        goToLocation(MuseumLat, MuseumLng, MuseumZoom);
        museumMarker = setMarker("Galleries of Justice Museum", MuseumLat, MuseumLng);
        museumMarker.setIcon(museumIcon);
    }

    private void goToLocation(double lat, double lng, int zoom) {

        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);
    }

    private void getCurrentLocation() {

        LatLng here = new LatLng(latitude, longitude);

        if (locatiomMarker != null) { locatiomMarker.remove(); }
        locatiomMarker = setMarker("You Are Here!", latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
    }

    private Marker setMarker(String location, double lat, double lng) {

        Marker marker = null;

        MarkerOptions markerOptions = new MarkerOptions()
                .title(location)
                .position(new LatLng(lat, lng));

        marker = mMap.addMarker(markerOptions);

        return marker;
    }
}
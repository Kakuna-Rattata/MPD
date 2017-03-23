package com.example.shann.galleriesofjustice;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by N0499010 Shannon Hibbett on 20/03/2017.
 *
 * GPSTracker class code adapted from :
 * http://wintechtutorials.com/blog/android-gps-location-manager-and-google-map-tutorial/
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context context;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    Location location;
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if ( !(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    || !(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) ) {

                ActivityCompat.requestPermissions(GlobalClass.getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);
            } else {
                if (isGPSEnabled) {

                    if (location == null) {
                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

                        if (locationManager != null ) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }

                if (location == null) {     //  If Location not found from GPS, find from Network
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(getApplicationContext().toString(), "Exception", e);
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

        //remove location callback:
        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onStatusChanged(String Provider, int status, Bundle extras) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

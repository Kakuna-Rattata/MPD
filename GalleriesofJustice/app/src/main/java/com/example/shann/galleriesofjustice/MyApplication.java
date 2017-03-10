package com.example.shann.galleriesofjustice;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

/*
 * Created by N0499010 on 24/02/2017.
 *
 * Beacon SDK and code from Estimote, tutorial :
 * http://developer.estimote.com/android/tutorial/part-1-setting-up/#prerequisites
 *
 * https://cloud.estimote.com/#/
 *
 * https://community.estimote.com/hc/en-us/articles/206851847-I-cannot-see-my-beacons-in-Estimote-Cloud-dashboard-How-do-I-add-them-?utm_source=Beacons%20Blank%20Slate&utm_medium=referral&utm_content=Transfer%20Beacons&utm_campaign=Cloud
 */

/* Application: Base class for those who need to maintain global application state.
*  Required for managing Beacons from any Activity in the app. */
public class MyApplication extends Application {

    private BeaconManager beaconManager;

    long scanDurInterval = 5000;
    long scanWaitInterval = 5000;

    /*  Estimote Region definitions for iBeacon Ranging :   */
    final Region regionAll = new Region(
            "All beacons",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
            null, null);    //  Target entire groups of beacons by setting the major and/or minor to null.
    final  Region regionCandy = new Region(
            "Candy beacon",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
            17236, 25458);  // iBeacon format Major, minor values to identify particular beacon

    @Override
    public void onCreate() {
        super.onCreate();

        /*  Firebase database setup : */
        if(!FirebaseApp.getApps(this).isEmpty()) {

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        /*  iBeacon Monitoring :    */
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setBackgroundScanPeriod(scanDurInterval, scanWaitInterval);   // Set enter/exit event trigger duration and wait time to 5 seconds

        // Create a beacon region defining monitoring geofence :
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(regionAll);
                beaconManager.startMonitoring(regionCandy);
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {

                if (region == regionAll) {
                    showNotification(
                            "Welcome to The Galleries of Justice Museum", // Title
                            "Come inside and try our new interactive tour!",  // Message
                            MainActivity.class
                    );

                    Log.d("monitoring: enter", region.toString());
                }
                else if (region == regionCandy) {
                    showNotification(
                            "Convict Ship", // Title
                            "check out this exhibit",   // Message
                            ExhibitActivity.class
                    );

                    Log.d("monitoring: enter", region.toString());
                }
            }

            @Override
            public void onExitedRegion(Region region) {

                if (region == regionAll) {
                    showNotification(
                            "Thank you for shopping with us",
                            "Check back soon for the latest " +
                                    "app instore discounts",
                            MainActivity.class
                    );

                    Log.d("monitoring: exit", region.toString());
                }
            }
        });
    }

    /* Add a notification to show up whenever
     * user enters the range of our monitored beacon. */
    public void showNotification(String title, String message, Class intentActivityClass) {

        Intent notificationIntent = new Intent(this, intentActivityClass);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[] {notificationIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}

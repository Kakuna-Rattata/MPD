package com.example.shann.galleriesofjustice;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * Created by N0499010 on 24/02/2017.
 *
 * Beacon SDK and code from Estimote, tutorial :
 * http://developer.estimote.com/android/tutorial/part-1-setting-up/#prerequisites
 *
 * https://cloud.estimote.com/#/
 */

/* Application: Base class for those who need to maintain global application state.
*  Required for managing Beacons from any Activity in the app. */
public class MyApplication extends Application {

    SharedPreferences preferences = null;

    private BeaconManager beaconManager;
    long scanDurInterval = 5000;
    long scanWaitInterval = 3000;

    /*  Estimote Region definitions for iBeacon Ranging :   */
    final Region regionAll = new Region(
            "All beacons",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
            null, null);    //  Target entire groups of beacons by setting the major and/or minor to null.
    final  Region regionLemon = new Region(
            "Lemon beacon",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
            28651, 37405);  // iBeacon format Major, minor values to identify particular beacon
    final  Region regionBeetroot = new Region(
            "Beetroot beacon",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
            18129, 1432);

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);

        final Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /*  Firebase database setup : */
        if(!FirebaseApp.getApps(this).isEmpty()) {

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        /*  iBeacon Monitoring :    */
        beaconManager = new BeaconManager(getApplicationContext());
        // Set enter/exit event trigger duration and wait time to 5 seconds :
        beaconManager.setBackgroundScanPeriod(scanDurInterval, scanWaitInterval);

        // Create a beacon region defining monitoring geofence :
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(regionAll);

                //  Unlocks "Adventurer" Achievement once beacon monitoring enabled (if not already unlocked)
                if (preferences.getBoolean(getString(R.string.achievements_adventure), false) == false) {
                    preferences.edit().putBoolean(getString(R.string.achievements_adventure), true).apply();
                    Intent achievementIntent = new Intent(getApplicationContext(), AchievementsActivity.class);
                    achievementIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    GlobalClass.showNotification(getString(R.string.achievement_unlocked) + ": " + getString(R.string.achievements_adventure), getString(R.string.achievements_adventure_criteria), achievementIntent, getApplicationContext(), GlobalClass.NOTIFICATION_ACHIEVEMENT);
                }
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {

                Log.d("monitoring: enter", region.toString());
                Log.d("region identifier", region.getIdentifier());

                //  Get beacon's MajorMinor key
                String beaconKey = String.format("%d:%d", region.getMajor(), region.getMinor());

                if (region == regionAll) {
                    GlobalClass.showNotification(
                            getString(R.string.welcome_notification_title),       // Title
                            getString(R.string.welcome_notification_contents),    // Message
                            mainIntent,                                           // Open this intent on tap
                            getApplicationContext(),                              // Current context
                            GlobalClass.NOTIFICATION_BEACON                       // Category of notification
                    );

                    beaconManager.startMonitoring(regionLemon);
                    beaconManager.startMonitoring(regionBeetroot);
                }
                else {
                    //  Trigger Exhibit Activity, provide region's MM key
                    Intent exhibitIntent = new Intent(getApplicationContext(), ExhibitActivity.class);
                    exhibitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    exhibitIntent.putExtra("beaconKey", beaconKey);

                    //getApplicationContext().getClass() != MainActivity.class
                    if ( getActivity() != null ) {

                        if ( getActivity().getClass() != (QuizActivity.class) )  {

                            startActivity(exhibitIntent);
                        }
                        else {

                            if (region == regionLemon) {

                                GlobalClass.showNotification(
                                        getString(R.string.exhibit_prison_cells),
                                        getString(R.string.exhibit_notification_contents),
                                        exhibitIntent,
                                        getApplicationContext(),
                                        GlobalClass.NOTIFICATION_BEACON
                                );
                            } else if (region == regionBeetroot) {

                                GlobalClass.showNotification(
                                        getString(R.string.exhibit_courtroom),
                                        getString(R.string.exhibit_notification_contents),
                                        exhibitIntent,
                                        getApplicationContext(),
                                        GlobalClass.NOTIFICATION_BEACON
                                );
                            }
                        }
                    }
                    else {  //  Only trigger Activities when App is running in foreground, else use notification

                        if (region == regionLemon) {

                            GlobalClass.showNotification(
                                    getString(R.string.exhibit_prison_cells),
                                    getString(R.string.exhibit_notification_contents),
                                    exhibitIntent,
                                    getApplicationContext(),
                                    GlobalClass.NOTIFICATION_BEACON
                            );
                        } else if (region == regionBeetroot) {

                            GlobalClass.showNotification(
                                    getString(R.string.exhibit_courtroom),
                                    getString(R.string.exhibit_notification_contents),
                                    exhibitIntent,
                                    getApplicationContext(),
                                    GlobalClass.NOTIFICATION_BEACON
                            );
                        }
                    }

                }
            }

            @Override
            public void onExitedRegion(Region region) {

                if (region == regionAll) {
                    GlobalClass.showNotification(
                            getString(R.string.notification_leaving_text),
                            getString(R.string.notification_leaving_contents),
                            GlobalClass.notifyIntent,
                            getApplicationContext(),
                            GlobalClass.NOTIFICATION_BEACON
                    );

                    Log.d("monitoring: exit", region.toString());
                }
            }
        });
    }

    public static Activity getActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object activityThread = null;
        try {
            activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Field activitiesField = null;
        try {
            activitiesField = activityThreadClass.getDeclaredField("mActivities");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        activitiesField.setAccessible(true);

        Map<Object, Object> activities = null;
        try {
            activities = (Map<Object, Object>) activitiesField.get(activityThread);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(activities == null)
            return null;

        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = null;
            try {
                pausedField = activityRecordClass.getDeclaredField("paused");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            pausedField.setAccessible(true);
            try {
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

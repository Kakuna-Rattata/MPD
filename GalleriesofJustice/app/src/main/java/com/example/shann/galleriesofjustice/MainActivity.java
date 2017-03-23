package com.example.shann.galleriesofjustice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static com.example.shann.galleriesofjustice.GlobalClass.getActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences = null;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_main);

        displaySelectedScreen(R.id.nav_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_call);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0115 952 0555"));
                startActivity(callIntent);
            }
        });

        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);
        if (preferences.getBoolean("firstrun", true)) {

            preferences.edit().putBoolean(getString(R.string.tour_activated), false).commit();

            preferences.edit().putBoolean(getString(R.string.achievements_quizmaster), false).commit();
            preferences.edit().putBoolean(getString(R.string.achievements_newexplorer), false).commit();
            preferences.edit().putBoolean(getString(R.string.achievements_tourguide), false).commit();
            preferences.edit().putBoolean(getString(R.string.achievements_adventure), false).commit();

            preferences.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(this, info_HelpActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, info_AboutActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            navigationView.clearFocus();
            navigationView.setCheckedItem(R.id.nav_map);
            //  Request enable GPS if disabled :
            int off = 0;
            try {
                off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if (off == 0) {
                showDialogGPS();

            } else {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }

            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_achievements) {
            navigationView.clearFocus();
            navigationView.setCheckedItem(R.id.nav_achievements);

            Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            navigationView.clearFocus();
            navigationView.setCheckedItem(R.id.nav_feedback);

            Context context = getActivity();
            GlobalClass.onShareClick(context);

        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            displaySelectedScreen(item.getItemId());
        }

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        //  Init selected fragment object :
        switch (itemId) {
            case R.id.nav_main:
                navigationView.clearFocus();
                navigationView.setCheckedItem(R.id.nav_main);

                fragment = new MainFragment();
                break;
            case R.id.nav_exhibitions:
                navigationView.clearFocus();
                navigationView.setCheckedItem(R.id.nav_exhibitions);

                fragment = new ExhibitionsFragment();
                break;
            case R.id.nav_events:
                navigationView.clearFocus();
                navigationView.setCheckedItem(R.id.nav_events);

                fragment = new EventsFragment();
                break;
        }

        //  Replacing the fragment :
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Show a dialog to the user requesting that GPS be enabled
     */
    private void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}



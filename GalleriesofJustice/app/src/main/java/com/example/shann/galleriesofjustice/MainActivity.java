package com.example.shann.galleriesofjustice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.sdk.SystemRequirementsChecker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences = null;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_main);


        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);

        if (preferences.getBoolean("firstrun", true)) {

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

//        if (id == R.id.nav_home) {
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_exhibitions) {
//            Intent intent = new Intent(MainActivity.this, info_ExhibitionsActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_events) {
//            Intent intent = new Intent(MainActivity.this, info_EventsActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_map) {
//            Intent intent = new Intent(MainActivity.this, MapActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_tour) {
//            //TODO: get current activity on top if ExhibitActivity or QuizActivity/QuizResultActivity
////            Intent intent = new Intent(MainActivity.this, info_TourActivity.class);
////            startActivity(intent);
//
//        } else if (id == R.id.nav_achievements) {
//            Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_feedback) {
//            Context context = GlobalClass.getActivity();
//            GlobalClass.onShareClick(context);
//        }

        if (id == R.id.nav_map) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tour) {
            //TODO: get current activity on top if ExhibitActivity or QuizActivity/QuizResultActivity
//            Intent intent = new Intent(MainActivity.this, info_TourActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_achievements) {
            Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            Context context = GlobalClass.getActivity();
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
                fragment = new MainFragment();
                break;
            case R.id.nav_exhibitions:
                fragment = new ExhibitionsFragment();
                break;
            case R.id.nav_events:
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

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
//
//        if (preferences.getBoolean("firstrun", true)) {
//
//            preferences.edit().putBoolean("Quiz Master", false).commit();
//            preferences.edit().putBoolean("New Explorer", false).commit();
//            preferences.edit().putBoolean("Tour Guide", false).commit();
//            preferences.edit().putBoolean("Adventurer", false).commit();
//
//            preferences.edit().putBoolean("firstrun", false).commit();
//        }
    }
}

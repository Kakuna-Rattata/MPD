package com.example.shann.galleriesofjustice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.example.shann.galleriesofjustice.MyApplication.getActivity;

public class AchievementsActivity extends AppCompatActivity {

    SharedPreferences preferences;

    private ImageButton imgButton1;
    private ImageButton imgButton2;
    private ImageButton imgButton3;
    private ImageButton imgButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        imgButton1 = (ImageButton) findViewById(R.id.imgBtn1);
        imgButton2 = (ImageButton) findViewById(R.id.imgBtn2);
        imgButton3 = (ImageButton) findViewById(R.id.imgBtn3);
        imgButton4 = (ImageButton) findViewById(R.id.imgBtn4);

        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);
        preferences.getAll();

        getAchievements();

        imgButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Quiz Master: 100% correct answers achieved on an exhibit quiz",
                            Toast.LENGTH_LONG).show();
            }
        });

        imgButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "New Explorer: Discovered first Exhibit",
                        Toast.LENGTH_LONG).show();
            }
        });

        imgButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Tour Guide: Used the Map",
                        Toast.LENGTH_LONG).show();
            }
        });

        imgButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Adventurer: Started Interactive Tour",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getAchievements() {

        if (preferences.getBoolean("Quiz Master", true)) {
            //  Unlocked when 100% scored on a Quiz result
            imgButton1.setImageResource(R.drawable.trophy);
        } else {
            imgButton1.setImageResource(R.drawable.lock);
        }

        if (preferences.getBoolean("New Explorer", true)) {
            //  Unlocked when ExhibitActivity staretd for first time
            imgButton2.setImageResource(R.drawable.trophy);
        } else {
            imgButton2.setImageResource(R.drawable.lock);
        }

        if (preferences.getBoolean("Tour Guide", true)) {
            //TODO: Unlocked when MapActivity started for first time
            imgButton3.setImageResource(R.drawable.trophy);
        } else {
            imgButton3.setImageResource(R.drawable.lock);
        }

        if (preferences.getBoolean("Adventurer", true)) {
            //TODO: (on MainActivity Button Press) Unlocked when Interactive Tour activated
            imgButton4.setImageResource(R.drawable.trophy);
        } else {
            imgButton4.setImageResource(R.drawable.lock);
        }


    }
}

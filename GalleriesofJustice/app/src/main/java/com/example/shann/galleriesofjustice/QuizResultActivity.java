package com.example.shann.galleriesofjustice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizResultActivity extends AppCompatActivity {

    Bundle extras;
    SharedPreferences preferences;

    private TextView textViewScore;

    private int score;
    private ArrayList<Question> qList;

    //public Achievement achievement;
    //public ArrayList<Achievement> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);

        textViewScore = (TextView) findViewById(R.id.textView_Score);

        extras = getIntent().getExtras();
        score = getIntent().getIntExtra("score", 0);
        textViewScore.setText(String.valueOf(score));

        //Exhibit exhibit = (Exhibit) extras.getSerializable("ExhibitObj");
        qList = (ArrayList<Question>) extras.get("questionList");
        if (score == qList.size()) {
            preferences.edit().putBoolean("Quiz Master", true).apply();    // Set 100% Quiz Score Achievement
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        extras = getIntent().getExtras();

        // Return to Exhibit Activity, restoring its previous state
        Intent intent = super.getParentActivityIntent();
        intent.putExtra("beaconKey", extras.getString("beaconKey"));

        Log.d("getString", extras.getString("beaconKey").toString());

        return intent;
    }
}

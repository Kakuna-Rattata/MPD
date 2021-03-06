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

    private TextView textViewScoreLabel;
    private TextView textViewScore;

    private int score;
    private ArrayList<Question> qList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);

        textViewScoreLabel = (TextView) findViewById(R.id.textView_ScoreLabel);
        textViewScore = (TextView) findViewById(R.id.textView_Score);

        extras = getIntent().getExtras();
        score = getIntent().getIntExtra("score", 0);
        textViewScore.setText(String.valueOf(score));

        textViewScoreLabel.announceForAccessibility(textViewScoreLabel.getText());
        textViewScore.announceForAccessibility(textViewScore.getText());

        //Exhibit exhibit = (Exhibit) extras.getSerializable("ExhibitObj");
        qList = (ArrayList<Question>) extras.get("questionList");
        if (score == qList.size()) {

            if (preferences.getBoolean(getString(R.string.achievements_quizmaster), false) == false) {
                preferences.edit().putBoolean(getString(R.string.achievements_quizmaster), true).apply();    // Set 100% Quiz Score Achievement
                Intent achievementIntent = new Intent(getApplicationContext(), AchievementsActivity.class);
                achievementIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalClass.showNotification(getString(R.string.achievement_unlocked) + ": " + getString(R.string.achievements_quizmaster), getString(R.string.achievements_quizmaster_criteria), achievementIntent, getApplicationContext(), GlobalClass.NOTIFICATION_ACHIEVEMENT);
            }
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

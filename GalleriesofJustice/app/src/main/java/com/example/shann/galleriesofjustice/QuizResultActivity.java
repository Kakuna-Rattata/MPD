package com.example.shann.galleriesofjustice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class QuizResultActivity extends AppCompatActivity {

    TextView textViewScore;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        textViewScore = (TextView) findViewById(R.id.textView_Score);

        score = getIntent().getIntExtra("score", 0);

        textViewScore.setText(String.valueOf(score));



    }
}

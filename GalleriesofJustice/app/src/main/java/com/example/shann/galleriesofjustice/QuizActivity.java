package com.example.shann.galleriesofjustice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.shann.galleriesofjustice.ExhibitActivity.REQUEST_CODE;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;

    private RadioGroup radioGroup;
    private RadioButton radioButtonA;
    private RadioButton radioButtonB;
    private RadioButton radioButtonC;

    private Button btnNext;

    private Bundle extras;
    private String beaconKey;

    private Exhibit exhibit;

    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private RadioButton radioButton_selected;
    private int score;

    private FirebaseDatabase database;
    private DatabaseReference dbRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        extras = getIntent().getExtras();
        beaconKey = extras.getString("beaconKey");

        initialize();   // Initialize UI, objects, containers etc.

        //getQuestion();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verifyAnswer()) {

                    score++;    //  Increase score
                    Log.d("score: ", String.valueOf(score));
                }

                advanceQuestion();
            }
        });
    }

    private void initialize() {

        FirebaseApp.initializeApp(getApplicationContext());

        database = FirebaseDatabase.getInstance();
        dbRootRef = database.getReference();

        textViewQuestion = (TextView) findViewById(R.id.textView_question) ;

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonA = (RadioButton) findViewById(R.id.radioButtonA);
        radioButtonB = (RadioButton) findViewById(R.id.radioButtonB);
        radioButtonC = (RadioButton) findViewById(R.id.radioButtonC);

        btnNext = (Button) findViewById(R.id.btn_next);

        exhibit = new Exhibit();
        exhibit = (Exhibit) getIntent().getSerializableExtra("ExhibitObj");

        setTitle(exhibit.getTitle() + " Quiz");

        textViewQuestion.setText(exhibit.getTitle().toString());

        questions = new ArrayList<Question>();

        dbRootRef.child(beaconKey).child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //questions = new ArrayList<Question>();

                for (DataSnapshot qChild : dataSnapshot.getChildren()) {
                    questions.add(qChild.getValue(Question.class));
                }

                currentQuestionIndex = 0;
                displayQuestion(currentQuestionIndex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayQuestion(int currentQuestionIndex) {

        textViewQuestion.setText(questions.get(currentQuestionIndex).getQuestionText());

        radioButtonA.setText(questions.get(currentQuestionIndex).getChoiceA());
        radioButtonB.setText(questions.get(currentQuestionIndex).getChoiceB());
        radioButtonC.setText(questions.get(currentQuestionIndex).getChoiceC());

        radioGroup.clearCheck();
    }

    private void advanceQuestion() {

        //currentQuestionIndex = (currentQuestionIndex + 1) % questions.size();
        currentQuestionIndex = (currentQuestionIndex + 1);

        if (currentQuestionIndex < questions.size()) {

            displayQuestion(currentQuestionIndex);

        } else {
            //  Go to Result Activity
            Intent resultIntent = new Intent(getApplicationContext(), QuizResultActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            resultIntent.putExtra("beaconKey", beaconKey);
            resultIntent.putExtra("ExhibitObj", exhibit);
            resultIntent.putExtra("score", score);

            startActivityForResult(resultIntent, REQUEST_CODE);
        }
    }

    protected boolean verifyAnswer() {

        String answer = "x";
        int id = radioGroup.getCheckedRadioButtonId();

        radioButton_selected = (RadioButton) findViewById(id);
        if (radioButton_selected == radioButtonA) answer = "A";
        if (radioButton_selected == radioButtonB) answer = "B";
        if (radioButton_selected == radioButtonC) answer = "C";

        return questions.get(currentQuestionIndex).isCorrectAnswer(answer);
    }

    protected Question getQuestion() {

        dbRootRef.child(beaconKey).child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //questions = new ArrayList<Question>();

                for (DataSnapshot qChild : dataSnapshot.getChildren()) {
                    questions.add(qChild.getValue(Question.class));
                }

                currentQuestionIndex = 1;
                displayQuestion(currentQuestionIndex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    @Override
    public Intent getParentActivityIntent() {
        extras = getIntent().getExtras();

        Intent intent = super.getParentActivityIntent();
        intent.putExtra("beaconKey", extras.getString("beaconKey"));

        Log.d("getString", extras.getString("beaconKey").toString());

        return intent;
    }

}

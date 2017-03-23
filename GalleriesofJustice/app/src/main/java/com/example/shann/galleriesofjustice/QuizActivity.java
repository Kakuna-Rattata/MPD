package com.example.shann.galleriesofjustice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.shann.galleriesofjustice.ExhibitActivity.REQUEST_CODE;

/**
 * Created by N0499010 Shannon Hibbett on 16/03/2017.
 *
 * Some Question and Quiz feature code adapted from tutorial https://www.youtube.com/watch?v=016QnvN5x4s
 */

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity: ";

    private TextView textViewQuestion;
    private ImageView imageViewQuestion;
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
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        extras = getIntent().getExtras();
        beaconKey = extras.getString("beaconKey");

        initialize();   // Initialize UI, objects, containers etc.

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    //  No radiobuttons checked
                    btnNext.setEnabled(false);
                } else {
                    // a radiobutton in the group is checked
                    btnNext.setEnabled(true);
                }
            }
        });

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

        database            = FirebaseDatabase.getInstance();
        dbRootRef           = database.getReference();

        textViewQuestion    = (TextView) findViewById(R.id.textView_question) ;
        imageViewQuestion   = (ImageView) findViewById(R.id.imageView_questionBg);
        radioGroup          = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonA        = (RadioButton) findViewById(R.id.radioButtonA);
        radioButtonB        = (RadioButton) findViewById(R.id.radioButtonB);
        radioButtonC        = (RadioButton) findViewById(R.id.radioButtonC);
        btnNext             = (Button) findViewById(R.id.btn_next);

        FirebaseApp.initializeApp(getApplicationContext());
        final StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef = storageRef.child("GOJ_Image_Content");

        exhibit = new Exhibit();
        exhibit = (Exhibit) getIntent().getSerializableExtra("ExhibitObj");
        setTitle(exhibit.getTitle() + " Quiz");
        String qImg = exhibit.getImage();
        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(imagesRef.child(qImg))
                .into(imageViewQuestion);

        getQuestion();
    }

    private void displayQuestion(int currentQuestionIndex) {

        textViewQuestion.setText(questions.get(currentQuestionIndex).getQuestionText());

        textViewQuestion.setContentDescription(textViewQuestion.getText());
        textViewQuestion.announceForAccessibility(textViewQuestion.getText());

        radioButtonA.setText(questions.get(currentQuestionIndex).getChoiceA());
        radioButtonB.setText(questions.get(currentQuestionIndex).getChoiceB());
        radioButtonC.setText(questions.get(currentQuestionIndex).getChoiceC());

        radioGroup.clearCheck();
    }

    private void advanceQuestion() {

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
            resultIntent.putExtra("questionList", questions);

            startActivityForResult(resultIntent, REQUEST_CODE);
        }
    }

    protected boolean verifyAnswer() {

        String answer = "x";
        int id = radioGroup.getCheckedRadioButtonId();

        radioButton_selected = (RadioButton) findViewById(id);
        if (radioButton_selected.isChecked() == true) {
            btnNext.setEnabled(true);
            if (radioButton_selected == radioButtonA) answer = "A";
            if (radioButton_selected == radioButtonB) answer = "B";
            if (radioButton_selected == radioButtonC) answer = "C";
        }

        return questions.get(currentQuestionIndex).isCorrectAnswer(answer);
    }

    protected void getQuestion() {

        btnNext.setEnabled(false);

        questions = new ArrayList<Question>();

        dbRootRef.child(beaconKey).child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot qChild : dataSnapshot.getChildren()) {
                    questions.add(qChild.getValue(Question.class));
                }

                currentQuestionIndex = 0;
                displayQuestion(currentQuestionIndex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

    }

    @Override
    public Intent getParentActivityIntent() {
        extras = getIntent().getExtras();

        Intent intent = super.getParentActivityIntent();
        intent.putExtra("beaconKey", extras.getString("beaconKey"));

        Log.d("getString", extras.getString("beaconKey").toString());

        return intent;
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}

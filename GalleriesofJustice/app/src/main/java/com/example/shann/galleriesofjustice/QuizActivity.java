package com.example.shann.galleriesofjustice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {

    TextView textViewTitle;
    ListView listViewAnswers;
    Button btnPrev;
    Button btnNext;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewTitle = (TextView) findViewById(R.id.textView_title) ;
        listViewAnswers = (ListView) findViewById(R.id.listViewAnswers);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnNext = (Button) findViewById(R.id.btn_next);


    }

    @Override
    public Intent getParentActivityIntent() {
        extras = getIntent().getExtras();

        Intent intent = super.getParentActivityIntent();
        intent.putExtra("beaconKey", extras.getString("beaconKey"));

        Log.d("getString", extras.getString("beaconKey").toString());

        return intent;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        Intent intent = new Intent();
//        intent.putExtra("beaconKey", extras);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//    }

}

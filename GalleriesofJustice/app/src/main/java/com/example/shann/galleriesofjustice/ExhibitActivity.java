package com.example.shann.galleriesofjustice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ExhibitActivity extends AppCompatActivity {

    private static final String TAG = "ExhibitActivity: ";
    public static final int REQUEST_CODE = 1;

    ImageView imageView;
    TextView textViewTitle;
    TextView textViewDesc;
    Button buttonQuiz;

    //  Declare Firebase Database elements:
    private static final String FIREBASE_URL = "https://galleriesofjustice-a9d86.firebaseio.com/";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRootRef = database.getReference();

    FirebaseStorage storage = FirebaseStorage.getInstance();

    String beaconKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_activity);

        Log.d(TAG, "onCreate() called");

        imageView = (ImageView) findViewById(R.id.imageView_Exhibit);
        textViewTitle = (TextView) findViewById(R.id.textView_title);
        textViewDesc = (TextView) findViewById(R.id.textView_desc);
        buttonQuiz = (Button) findViewById(R.id.btn_Quiz);

        //  Get beaconKey passed from triggering Activity/Notification :
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            beaconKey = extras.getString("beaconKey");
        }
        Log.d(TAG, "beaconKey is: " + beaconKey);

        // Reference to an image file in Firebase Storage
        final StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef = storageRef.child("GOJ_Image_Content");

        final Exhibit exhibit = new Exhibit();
        //  Query db for page content :
        dbRootRef.child(beaconKey).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgName = null, title = null, desc = null;
                if (dataSnapshot.getValue() != null) {

                    exhibit.setKey(beaconKey);

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("image")) {
                            imgName = child.getValue().toString();
                            exhibit.setImage(imgName);
                        }
                        if (child.getKey().equals("title")) {
                            title = child.getValue().toString();
                            exhibit.setTitle(title);
                        }
                        if (child.getKey().equals("desc")) {
                            desc = child.getValue().toString();
                            exhibit.setDesc(desc);
                        }
                    }

                    Glide.with(getApplicationContext() /* context */)
                            .using(new FirebaseImageLoader())
                            .load(imagesRef.child(imgName))
                            .into(imageView);

                    textViewTitle.setText(title);
                    textViewDesc.setText(desc);
                }
            }

                @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        buttonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent quizIntent = new Intent(getApplicationContext(), QuizActivity.class);
                quizIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                quizIntent.putExtra("beaconKey", beaconKey);
                quizIntent.putExtra("ExhibitObj", exhibit);

                startActivityForResult(quizIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle extras = getIntent().getExtras();
        outState.putAll(extras);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        beaconKey = savedInstanceState.getString("beaconKey");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("beaconKey");
                // do something with the result

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }
    }
}

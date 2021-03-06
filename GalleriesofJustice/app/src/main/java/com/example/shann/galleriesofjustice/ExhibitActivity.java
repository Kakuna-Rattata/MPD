package com.example.shann.galleriesofjustice;

import android.content.Intent;
import android.content.SharedPreferences;
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

    public static final int REQUEST_CODE = 1;
    private static final String TAG = "ExhibitActivity: ";

    SharedPreferences preferences;

    private ImageView imageView;
    private TextView textViewTitle;
    private TextView textViewDesc;
    private Button buttonQuiz;

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

        // Set New Explorer Achievement for discovering Exhibit for first time
        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);

        if (preferences.getBoolean(getString(R.string.achievements_newexplorer), false) == false) {
            preferences.edit().putBoolean(getString(R.string.achievements_newexplorer), true).apply();
            Intent achievementIntent = new Intent(getApplicationContext(), AchievementsActivity.class);
            achievementIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GlobalClass.showNotification(
                    getString(R.string.achievement_unlocked) + ": " + getString(R.string.achievements_newexplorer),
                    getString(R.string.achievements_newexplorer_criteria),
                    achievementIntent,
                    getApplicationContext(),
                    GlobalClass.NOTIFICATION_ACHIEVEMENT);
        }

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

        // Create Reference to the app's image file storage in Firebase Storage
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

                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(imagesRef.child(imgName))
                            .into(imageView);

                    setTitle(title);
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
    public void onBackPressed() {
        super.onBackPressed();

        finish();
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
}

package com.example.shann.galleriesofjustice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ExhibitActivity extends AppCompatActivity {

    private static final String TAG = "Lifecycle: ";

    private ImageView imageView;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_activity);

        Log.d(TAG, "onCreate() called");

        // Reference to an image file in Firebase Storage
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("GOJ_Image_Content");

        imageView = (ImageView) findViewById(R.id.imageView_Exhibit);

        // Load the image using Glide
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(imagesRef.child("goj_courtroom_.jpg"))
                .into(imageView);
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
}

package com.example.shann.galleriesofjustice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.shann.galleriesofjustice.MyApplication.getActivity;

public class AchievementsActivity extends AppCompatActivity {

    SharedPreferences preferences;

    private ImageButton imgButton1;
    private ImageButton imgButton2;
    private ImageButton imgButton3;
    private ImageButton imgButton4;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    private String achievement1;
    private String achievement2;
    private String achievement3;
    private String achievement4;

    private String toastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        imgButton1 = (ImageButton) findViewById(R.id.imgBtn1);
        imgButton2 = (ImageButton) findViewById(R.id.imgBtn2);
        imgButton3 = (ImageButton) findViewById(R.id.imgBtn3);
        imgButton4 = (ImageButton) findViewById(R.id.imgBtn4);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        toastText = "";

        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = getSharedPreferences("com.example.shann.galleriesofjustice", MODE_PRIVATE);
        preferences.getAll();

        textView1.setText(getString(R.string.achievements_quizmaster) );
        textView2.setText(getString(R.string.achievements_newexplorer) );
        textView3.setText(getString(R.string.achievements_tourguide) );
        textView4.setText(getString(R.string.achievements_adventure) );

        imgButton1.setContentDescription("Selected " + getString(R.string.achievements_quizmaster) + " Achievement");
        imgButton2.setContentDescription("Selected " + getString(R.string.achievements_newexplorer) + " Achievement");
        imgButton3.setContentDescription("Selected " + getString(R.string.achievements_tourguide) + " Achievement");
        imgButton4.setContentDescription("Selected " + getString(R.string.achievements_adventure) + " Achievement");

        getAchievements();

        imgButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( preferences.getBoolean(getString(R.string.achievements_quizmaster), false) == false ) {
                    toastText = "Locked: " + getString(R.string.achievements_quizmaster_criteria);
                } else {
                    toastText = getString(R.string.achievements_quizmaster_criteria);
                }

                Toast.makeText(getActivity(), toastText,
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( preferences.getBoolean(getString(R.string.achievements_newexplorer), false) == false ) {
                    toastText = "Locked: " + getString(R.string.achievements_newexplorer_criteria);
                } else {
                    toastText = getString(R.string.achievements_newexplorer_criteria);
                }

                Toast.makeText(getActivity(), toastText,
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( preferences.getBoolean(getString(R.string.achievements_tourguide), false) == false ) {
                    toastText = "Locked: " + getString(R.string.achievements_tourguide_criteria);
                } else {
                    toastText = getString(R.string.achievements_tourguide_criteria);
                }

                Toast.makeText(getActivity(), toastText,
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( preferences.getBoolean(getString(R.string.achievements_adventure), false) == false ) {
                    toastText = "Locked: " + getString(R.string.achievements_adventure_criteria);
                } else {
                    toastText = getString(R.string.achievements_adventure_criteria);
                }

                Toast.makeText(getActivity(), toastText,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getAchievements() {

        if (preferences.getBoolean(getString(R.string.achievements_quizmaster), true)) {
            //  Unlocked when 100% scored on a Quiz result
            imgButton1.setImageResource(R.drawable.trophy);
            //  Trophy image drawable source: http://findicons.com/icon/596396/trophy
        } else {
            imgButton1.setImageResource(R.drawable.lock);
            //  Lock image drawable source: https://www.iconfinder.com/icons/286675/lock_icon
        }

        if (preferences.getBoolean(getString(R.string.achievements_newexplorer), true)) {
            //  Unlocked when ExhibitActivity staretd for first time
            imgButton2.setImageResource(R.drawable.trophy);
        } else {
            imgButton2.setImageResource(R.drawable.lock);
        }

        if (preferences.getBoolean(getString(R.string.achievements_tourguide), true)) {
            //  Unlocked when MapActivity started for first time
            imgButton3.setImageResource(R.drawable.trophy);
        } else {
            imgButton3.setImageResource(R.drawable.lock);
        }

        if (preferences.getBoolean(getString(R.string.achievements_adventure), true)) {
            //TODO: (on MainActivity Button Press) Unlocked when Interactive Tour activated
            imgButton4.setImageResource(R.drawable.trophy);
        } else {
            imgButton4.setImageResource(R.drawable.lock);
        }


    }
}

package com.example.hw1_airballoons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hw1_airballoons.Interfaces.Callback_highScoreClicked;
import com.example.hw1_airballoons.Views.MapsFragment;
import com.example.hw1_airballoons.Views.RecordsListFragment;
import com.google.android.material.button.MaterialButton;

public class RecordsTableActivity extends AppCompatActivity {
    public static final String RECORDS_TABLE = "RECORDS";
    private MaterialButton records_BTN_back;
    private MapsFragment mapsFragment;
    private RecordsListFragment recordsListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_table);

        findViews();

        mapsFragment = new MapsFragment();
        recordsListFragment = new RecordsListFragment();

        recordsListFragment.setCallbackHighScoreClicked(new Callback_highScoreClicked() {
            @Override
            public void highScoreClicked(String title, double lat, double lon) {
                if (mapsFragment != null)
                    mapsFragment.zoom(title, lat, lon);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.records_FRAME_list, recordsListFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRAME_map, mapsFragment).commit();

        records_BTN_back.setOnClickListener(view -> backToOpenningScreen());

    }


    private void findViews() {
        records_BTN_back = findViewById(R.id.records_BTN_back);
    }


    private void backToOpenningScreen() {
        Intent openingScreenActivity = new Intent(this, OpeningScreenActivity.class);
        startActivity(openingScreenActivity);
        finish();
    }
}
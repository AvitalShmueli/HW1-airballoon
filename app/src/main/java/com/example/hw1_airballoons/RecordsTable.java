package com.example.hw1_airballoons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.hw1_airballoons.Interfaces.Callback_highScoreClicked;
import com.example.hw1_airballoons.Models.RecordsList;
import com.example.hw1_airballoons.Utilities.SharedPreferencesManager;
import com.example.hw1_airballoons.Utilities.SignalManager;
import com.example.hw1_airballoons.Views.MapsFragment;
import com.example.hw1_airballoons.Views.RecordsListFragment;
import com.google.gson.Gson;

public class RecordsTable extends AppCompatActivity {
    public static final String RECORDS_TABLE = "RECORDS";
    private FrameLayout main_FRAME_list;
    private FrameLayout main_FRAME_map;

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
            public void highScoreClicked(double lat, double lon) {
                mapsFragment.zoom(lat,lon);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list,recordsListFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapsFragment).commit();

    }

    private void findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list);
        main_FRAME_map = findViewById(R.id.main_FRAME_map);
    }
}
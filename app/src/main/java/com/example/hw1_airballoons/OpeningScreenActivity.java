package com.example.hw1_airballoons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class OpeningScreenActivity extends AppCompatActivity {

    enum GameMode {BUTTONS,SENSORS}
    private ShapeableImageView open_IMG_background;
    private MaterialButton open_BTN_buttons;
    private MaterialButton open_BTN_sensors;
    private MaterialButton open_BTN_records;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        findViews();

        Glide
                .with(this)
                .load(R.drawable.sky)
                .centerCrop()
                .placeholder(R.drawable.background)
                .into(open_IMG_background);

        open_BTN_buttons.setOnClickListener(view-> startGame(GameMode.BUTTONS));
        open_BTN_sensors.setOnClickListener(view-> startGame(GameMode.SENSORS));
        open_BTN_records.setOnClickListener(view-> viewRecordsTable());
    }

    private void viewRecordsTable() {
        Intent recordsTableActivity = new Intent(this, RecordsTableActivity.class);
        startActivity(recordsTableActivity);
        finish();

    }

    private void startGame(GameMode gameMode) {
        Intent mainActivity = new Intent(this, MainActivity.class);
        Log.d("start game GameMode = "+String.valueOf(gameMode),"open");
        mainActivity.putExtra(MainActivity.KEY_MODE,gameMode.ordinal());
        startActivity(mainActivity);
        finish();
    }


    private void findViews() {
        open_IMG_background = findViewById(R.id.open_IMG_background);
        open_BTN_buttons = findViewById(R.id.open_BTN_buttons);
        open_BTN_sensors = findViewById(R.id.open_BTN_sensors);
        open_BTN_records = findViewById(R.id.open_BTN_records);
    }
}
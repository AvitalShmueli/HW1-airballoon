package com.example.hw1_airballoons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;


public class EndGameActivity extends AppCompatActivity {
    private ShapeableImageView main_IMG_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        findViews();

        Glide
                .with(this)
                .load(R.drawable.sky)
                .centerCrop()
                .placeholder(R.drawable.background)
                .into(main_IMG_background);


    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
    }


}
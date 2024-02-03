package com.example.hw1_airballoons;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {
    private ShapeableImageView main_IMG_background;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[] main_IMG_obstacles;
    private ShapeableImageView main_IMG_airballoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        Glide
                .with(this)
                .load(R.drawable.sky_background)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(main_IMG_background);


    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        main_IMG_obstacles = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_bird1),
                findViewById(R.id.main_IMG_bird2),
                findViewById(R.id.main_IMG_bird3),
                findViewById(R.id.main_IMG_bird4),
                findViewById(R.id.main_IMG_bird5),
                findViewById(R.id.main_IMG_bird6),
                findViewById(R.id.main_IMG_bird7),
                findViewById(R.id.main_IMG_bird8),
                findViewById(R.id.main_IMG_bird9),
                findViewById(R.id.main_IMG_bird10),
                findViewById(R.id.main_IMG_bird11),
                findViewById(R.id.main_IMG_bird12),
                findViewById(R.id.main_IMG_bird13),
                findViewById(R.id.main_IMG_bird14),
                findViewById(R.id.main_IMG_bird15)
        };
        main_IMG_airballoon = findViewById(R.id.main_IMG_airballoon);

    }
}
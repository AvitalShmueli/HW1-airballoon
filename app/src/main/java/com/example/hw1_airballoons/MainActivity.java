package com.example.hw1_airballoons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    enum Direction {LEFT,RIGHT}
    private ShapeableImageView main_IMG_background;

    private static final int LANES = 3;
    private static final int MAX_OBSTACLES = 6;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_obstacles;
    private ShapeableImageView[] main_IMG_airballoons;
    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private GameManager gameManager;

    private Timer timer;
    private static final long DELAY = 1000;
    private long startTime;
    private boolean timerOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        gameManager = new GameManager(main_IMG_hearts.length,LANES);
        initViews();
        startTimer();
        Glide
                .with(this)
                .load(R.drawable.sky)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(main_IMG_background);

        refreshUI(gameManager.getAirballoonIndex());

        main_BTN_left.setOnClickListener(view->moveAirballoon(Direction.LEFT));
        main_BTN_right.setOnClickListener(view->moveAirballoon(Direction.RIGHT));


    }

    private void moveAirballoon(Direction direction) {
        if(direction == Direction.LEFT && gameManager.getAirballoonIndex() != 0){
            refreshUI(gameManager.getAirballoonIndex()-1);

        }
        else if(direction == Direction.RIGHT && gameManager.getAirballoonIndex() != LANES-1){
            refreshUI(gameManager.getAirballoonIndex()+1);
        }

    }


    @Override
    protected void onStop() {
        // Don't forget to kill the timer!!!
        timerOn = false;
        timer.cancel();
        super.onStop();
    }


    private void refreshUI(int newCurrentIndex) {
        // set all airballoons visibility to false except the one in the middle
        gameManager.setAirballoonIndex(newCurrentIndex);
        for(int i = 0; i < LANES; i++)
        {
            if(i != newCurrentIndex)
                main_IMG_airballoons[i].setVisibility(View.INVISIBLE);
            else main_IMG_airballoons[i].setVisibility(View.VISIBLE);
        }


    }

    private void updateTimerUI() {
        Random rand = new Random();
        int rnd_col = rand.nextInt(LANES);
        main_IMG_obstacles[rnd_col][0].setVisibility(View.INVISIBLE);
    }

    private void initViews(){
        // set all obstacles visibility to false
        for(int i = 0; i < LANES; i++) {
            for(int j = 0; j < MAX_OBSTACLES; j++) {
                main_IMG_obstacles[i][j].setVisibility(View.INVISIBLE);
            }
        }

    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        main_IMG_obstacles = new ShapeableImageView[][]{
                {
                        findViewById(R.id.main_IMG_bird_00),
                        findViewById(R.id.main_IMG_bird_01),
                        findViewById(R.id.main_IMG_bird_02),
                        findViewById(R.id.main_IMG_bird_03),
                        findViewById(R.id.main_IMG_bird_04),
                        findViewById(R.id.main_IMG_bird_05)
                },
                {
                        findViewById(R.id.main_IMG_bird_10),
                        findViewById(R.id.main_IMG_bird_11),
                        findViewById(R.id.main_IMG_bird_12),
                        findViewById(R.id.main_IMG_bird_13),
                        findViewById(R.id.main_IMG_bird_14),
                        findViewById(R.id.main_IMG_bird_15)
                },
                {
                        findViewById(R.id.main_IMG_bird_20),
                        findViewById(R.id.main_IMG_bird_21),
                        findViewById(R.id.main_IMG_bird_22),
                        findViewById(R.id.main_IMG_bird_23),
                        findViewById(R.id.main_IMG_bird_24),
                        findViewById(R.id.main_IMG_bird_25)
                }
        };

        main_IMG_airballoons = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_airballoon1),
                findViewById(R.id.main_IMG_airballoon2),
                findViewById(R.id.main_IMG_airballoon3)
        };

        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);

    }

    private void startTimer() {
        if(!timerOn) {
            timerOn = true;
            startTime = System.currentTimeMillis();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(()->updateTimerUI()); // the only way to touch the views is via the UI thread (the main thread)
                }
            },0,DELAY);
        }
    }
}
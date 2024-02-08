package com.example.hw1_airballoons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    enum Direction {LEFT,RIGHT}
    private ShapeableImageView main_IMG_background;
    private static final int LANES = 3;
    private static final int MAX_OBSTACLES = 6;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_obstacles;
    private ShapeableImageView[] main_IMG_airBalloons;
    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private GameManager gameManager;
    private Timer timer;
    private static final long DELAY = 400;
    private boolean timerOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignalManager.init(this);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length,LANES,MAX_OBSTACLES);
        updateUI();

        Glide
                .with(this)
                .load(R.drawable.sky)
                .centerCrop()
                .placeholder(R.drawable.background)
                .into(main_IMG_background);

        startTimer();
        main_BTN_left.setOnClickListener(view-> moveAirBalloon(Direction.LEFT));
        main_BTN_right.setOnClickListener(view-> moveAirBalloon(Direction.RIGHT));

    }


    @Override
    protected void onStop() {
        // Don't forget to kill the timer!!!
        timerOn = false;
        timer.cancel();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        timerOn = false;
        timer.cancel();
        super.onDestroy();
    }


    private void moveAirBalloon(Direction direction) {
        if(direction == Direction.LEFT){
            gameManager.moveAirBalloonLeft();
        }
        else if(direction == Direction.RIGHT){
            gameManager.moveAirBalloonRight();
        }
        updatePlayerLocation();
    }


    private void updateUI(){
        gameManager.updateObstacles();
        boolean[][] obstacles = gameManager.getMatObstacles();
        for(int i = 0; i < LANES; i++) {
            for(int j = 0; j < MAX_OBSTACLES; j++) {
                main_IMG_obstacles[i][j].setVisibility(obstacles[i][j] ? View.VISIBLE : View.INVISIBLE);
            }
        }
        updatePlayerLocation();
    }


    private void updatePlayerLocation() {
        boolean[] airBalloons = gameManager.getAirBalloons();
        for(int i = 0; i < LANES; i++) {
            main_IMG_airBalloons[i].setVisibility(airBalloons[i] ? View.VISIBLE : View.INVISIBLE);
        }
        /* update hearts */
        int collisionsNum = gameManager.getCollisionsNum();
        if(collisionsNum != 0 && collisionsNum < gameManager.getLife())
            main_IMG_hearts[main_IMG_hearts.length - collisionsNum].setVisibility(View.INVISIBLE);
        else if(collisionsNum == gameManager.getLife()) {
            changeActivity();
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

        main_IMG_airBalloons = new ShapeableImageView[]{
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
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(()-> updateUI()); // the only way to touch the views is via the UI thread (the main thread)
                }
            },0,DELAY);
        }
    }


    private void changeActivity() {
        Intent endGameIntent = new Intent(this, EndGameActivity.class);
        startActivity(endGameIntent);
        timerOn = false;
        timer.cancel();
        finish();
    }


}
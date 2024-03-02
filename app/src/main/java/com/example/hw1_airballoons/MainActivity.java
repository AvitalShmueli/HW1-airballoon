package com.example.hw1_airballoons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.hw1_airballoons.Interfaces.Callback_tilt;
import com.example.hw1_airballoons.Utilities.TiltDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    enum Direction {LEFT, RIGHT}
    public static final String KEY_MODE = "KEY_GAME_MODE";
    private ShapeableImageView main_IMG_background;
    private static final int LANES = 5;
    private static final int MAX_OBSTACLES = 6;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_obstacles;
    private ShapeableImageView[] main_IMG_airBalloons;
    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private GameManager gameManager;
    private Timer timer;
    private MaterialTextView main_LBL_score;
    private static final long DELAY = 500;//350;
    private boolean timerOn = false;
    private TiltDetector tiltDetector;
    private MaterialTextView main_LBL_sensorsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SignalManager.init(this);

        Intent previousScreen = getIntent();
        int mode = previousScreen.getIntExtra(KEY_MODE, 0);
        Log.d("GameMode = " + mode, "GameMode = " +mode);

        findViews();

        gameManager = new GameManager(main_IMG_hearts.length, LANES, MAX_OBSTACLES);
        updateUI();

        Glide
                .with(this)
                .load(R.drawable.sky)
                .centerCrop()
                .placeholder(R.drawable.background)
                .into(main_IMG_background);

        startTimer();

        if (mode == OpeningScreenActivity.GameMode.BUTTONS.ordinal()) {
            Log.d("start game GameMode = " + mode, "buttons");
            main_BTN_left.setVisibility(View.VISIBLE);
            main_BTN_right.setVisibility(View.VISIBLE);
            main_LBL_sensorsText.setVisibility(View.INVISIBLE);
            main_BTN_left.setOnClickListener(view -> moveAirBalloon(Direction.LEFT));
            main_BTN_right.setOnClickListener(view -> moveAirBalloon(Direction.RIGHT));
        }
        else {
            Log.d("start game GameMode = " + mode, "sensors");
            main_BTN_left.setVisibility(View.INVISIBLE);
            main_BTN_right.setVisibility(View.INVISIBLE);
            main_LBL_sensorsText.setVisibility(View.VISIBLE);
            initTiltDetector();
        }

    }

    private void initTiltDetector() {
        tiltDetector = new TiltDetector(this, new Callback_tilt() {
            @Override
            public void tiltRight() {
                moveAirBalloon(Direction.RIGHT);
            }

            @Override
            public void tiltLeft() {
                moveAirBalloon(Direction.LEFT);
            }
        });
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


    @Override
    protected void onPause() {
        timerOn = false;
        if (tiltDetector != null)
            tiltDetector.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tiltDetector != null) {
            tiltDetector.start();
            startTimer();
        }
    }

    private void moveAirBalloon(Direction direction) {
        if (direction == Direction.LEFT) {
            gameManager.moveAirBalloonLeft();
        } else if (direction == Direction.RIGHT) {
            gameManager.moveAirBalloonRight();
        }
        updatePlayerLocation();
    }


    /*
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
    */

    private void updateUI() {
        gameManager.updateObstacles();
        int[][] obstacles = gameManager.getMatObstacles();
        for (int i = 0; i < LANES; i++) {
            for (int j = 0; j < MAX_OBSTACLES; j++) {
                if (obstacles[i][j] == 1)
                    main_IMG_obstacles[i][j].setImageResource(R.drawable.bird);
                else if (obstacles[i][j] == 2)
                    main_IMG_obstacles[i][j].setImageResource(R.drawable.firering2);
                else if (obstacles[i][j] == 3)
                    main_IMG_obstacles[i][j].setImageResource(R.drawable.heart);
                main_IMG_obstacles[i][j].setVisibility(obstacles[i][j] > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        }
        updatePlayerLocation();
    }


    private void updatePlayerLocation() {
        boolean[] airBalloons = gameManager.getAirBalloons();
        for (int i = 0; i < LANES; i++) {
            main_IMG_airBalloons[i].setVisibility(airBalloons[i] ? View.VISIBLE : View.INVISIBLE);
        }
        main_LBL_score.setText(String.valueOf(gameManager.getScore()));
        /* update hearts */
        int collisionsNum = gameManager.getCollisionsNum();
        int previousCollisionsNum = gameManager.getPreviousCollisionsNum();
        int life = gameManager.getLife();

        if (collisionsNum > previousCollisionsNum) {
            if (collisionsNum != 0 && collisionsNum < life) {
                Log.d("hearts - collisionsNum | previousCollisionsNum", collisionsNum + "|" + previousCollisionsNum);
                main_IMG_hearts[main_IMG_hearts.length - collisionsNum].setVisibility(View.INVISIBLE);
            } else if (collisionsNum == life) {
                main_IMG_hearts[main_IMG_hearts.length - collisionsNum].setVisibility(View.INVISIBLE);
                changeActivity();
            }
        } else if (collisionsNum < previousCollisionsNum) {
            if (collisionsNum >= 0 && collisionsNum < life) {
                Log.d("hearts - collisionsNum | previousCollisionsNum", collisionsNum + "|" + previousCollisionsNum);
                main_IMG_hearts[main_IMG_hearts.length - collisionsNum - 1].setVisibility(View.VISIBLE);
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
                },
                {
                        findViewById(R.id.main_IMG_bird_30),
                        findViewById(R.id.main_IMG_bird_31),
                        findViewById(R.id.main_IMG_bird_32),
                        findViewById(R.id.main_IMG_bird_33),
                        findViewById(R.id.main_IMG_bird_34),
                        findViewById(R.id.main_IMG_bird_35)
                },
                {
                        findViewById(R.id.main_IMG_bird_40),
                        findViewById(R.id.main_IMG_bird_41),
                        findViewById(R.id.main_IMG_bird_42),
                        findViewById(R.id.main_IMG_bird_43),
                        findViewById(R.id.main_IMG_bird_44),
                        findViewById(R.id.main_IMG_bird_45)
                }
        };

        main_IMG_airBalloons = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_airballoon1),
                findViewById(R.id.main_IMG_airballoon2),
                findViewById(R.id.main_IMG_airballoon3),
                findViewById(R.id.main_IMG_airballoon4),
                findViewById(R.id.main_IMG_airballoon5)
        };

        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_LBL_sensorsText = findViewById(R.id.main_LBL_sensorsText);

    }


    private void startTimer() {
        if (!timerOn) {
            timerOn = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> updateUI()); // the only way to touch the views is via the UI thread (the main thread)
                }
            }, 0, DELAY);
        }
    }


    private void changeActivity() {
        Intent endGameIntent = new Intent(this, EndGameActivity.class);
        endGameIntent.putExtra(EndGameActivity.KEY_SCORE, gameManager.getScore());
        startActivity(endGameIntent);
        timerOn = false;
        timer.cancel();
        finish();
    }


}
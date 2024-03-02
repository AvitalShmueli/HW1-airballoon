package com.example.hw1_airballoons;

import com.example.hw1_airballoons.Utilities.PlaySound;
import com.example.hw1_airballoons.Utilities.SignalManager;

import java.util.Random;

public class GameManager {
    private int life;
    private int lanes;
    private int max_obstacles;
    private int airBalloonIndex;
    private int[][] matObstacles;
    private boolean[] arrAirBalloons;
    private int collisionsNum;
    private int previousCollisionsNum;
    private final Random rand;
    private int score;
    private PlaySound crashSound;
    private PlaySound collectSound;
    private PlaySound heartSound;


    public GameManager(PlaySound crashSound, PlaySound collectSound, PlaySound heartSound) {
        this(3, 3, 6, crashSound, collectSound, heartSound);
    }


    public GameManager(int life, int lanes, int max_obstacles, PlaySound crashSound, PlaySound collectSound, PlaySound heartSound) {
        this.life = life;
        this.airBalloonIndex = lanes / 2;
        this.lanes = lanes;
        this.max_obstacles = max_obstacles;
        this.collisionsNum = 0;
        this.previousCollisionsNum = 0;
        this.score = 0;
        rand = new Random();
        this.crashSound = crashSound;
        this.collectSound = collectSound;
        this.heartSound = heartSound;
        initObstacles();
        initAirBalloons();
    }


    public int getLife() {
        return life;
    }


    public GameManager setLife(int life) {
        this.life = life;
        return this;
    }


    public int getLanes() {
        return lanes;
    }


    public GameManager setLanes(int lanes) {
        this.lanes = lanes;
        return this;
    }


    public int getMax_obstacles() {
        return max_obstacles;
    }


    public GameManager setMax_obstacles(int max_obstacles) {
        this.max_obstacles = max_obstacles;
        return this;
    }


    public GameManager setMatObstacles(int[][] matObstacles) {
        this.matObstacles = matObstacles;
        return this;
    }


    public int getAirBalloonIndex() {
        return airBalloonIndex;
    }


    public GameManager setAirBalloonIndex(int airBalloonIndex) {
        this.airBalloonIndex = airBalloonIndex;
        return this;
    }


    private void initObstacles() {
        this.matObstacles = new int[lanes][max_obstacles];
        for (int i = 0; i < lanes; i++)
            for (int j = 0; j < max_obstacles; j++)
                matObstacles[i][j] = 0;
    }


    public int[][] getMatObstacles() {
        return matObstacles;
    }


    public void updateObstacles() {
        int tmpScore = score;
        for (int i = 0; i < lanes; i++) {
            for (int j = max_obstacles - 1; j > 0; j--) {
                if (j == max_obstacles - 1 && matObstacles[i][j] > 0 && i == airBalloonIndex) {
                    checkCollision();
                    if (matObstacles[i][j - 1] > 0) {
                        matObstacles[i][j] = matObstacles[i][j - 1];
                        matObstacles[i][j - 1] = 0;
                    } else
                        matObstacles[i][j] = 0;
                } else {
                    matObstacles[i][j] = matObstacles[i][j - 1];
                    matObstacles[i][j - 1] = 0;
                }
            }
        }

        int rnd_col = rand.nextInt(lanes * 2);
        if (rnd_col < 5)
            matObstacles[rnd_col % lanes][0] = 1; // birds
        else if (rnd_col % 2 == 0 || collisionsNum == 0)
            matObstacles[rnd_col % lanes][0] = 2; // fire-rings
        else matObstacles[rnd_col % lanes][0] = 3; // hearts
    }


    private void initAirBalloons() {
        this.arrAirBalloons = new boolean[lanes];
        updateAirBalloons();
    }


    public boolean[] getAirBalloons() {
        return arrAirBalloons;
    }


    public GameManager setArrAirBalloons(boolean[] arrAirBalloons) {
        this.arrAirBalloons = arrAirBalloons;
        return this;
    }


    public int getScore() {
        return score;
    }

    public GameManager setScore(int score) {
        this.score = score;
        return this;
    }


    private void updateAirBalloons() {
        for (int i = 0; i < lanes; i++) {
            if (i != airBalloonIndex)
                arrAirBalloons[i] = false;
            else arrAirBalloons[i] = true;
        }
    }


    public void moveAirBalloonRight() {
        if (airBalloonIndex < lanes - 1) {
            airBalloonIndex++;
            updateAirBalloons();
        }
    }

    public void moveAirBalloonLeft() {
        if (airBalloonIndex > 0) {
            airBalloonIndex--;
            updateAirBalloons();
        }
    }


    private void checkCollision() {
        if (matObstacles[airBalloonIndex][max_obstacles - 1] == 1 && collisionsNum < life) {
            crashSound.start();
            this.previousCollisionsNum = this.collisionsNum;
            this.collisionsNum++;
            SignalManager.getInstance().vibrate(200);
            SignalManager.getInstance().toast("Oops");
        } else if (matObstacles[airBalloonIndex][max_obstacles - 1] == 2) {
            collectSound.start();
            this.score += 10;
        } else if (matObstacles[airBalloonIndex][max_obstacles - 1] == 3) {
            heartSound.start();
            this.previousCollisionsNum = this.collisionsNum;
            if (collisionsNum > 0)
                this.collisionsNum--;
        }

    }


    public int getCollisionsNum() {
        return collisionsNum;
    }


    public GameManager setCollisionsNum(int collisionsNum) {
        this.collisionsNum = collisionsNum;
        return this;
    }


    public int getPreviousCollisionsNum() {
        return previousCollisionsNum;
    }


    public GameManager setPreviousCollisionsNum(int previousCollisionsNum) {
        this.previousCollisionsNum = previousCollisionsNum;
        return this;
    }
}

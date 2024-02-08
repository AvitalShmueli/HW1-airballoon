package com.example.hw1_airballoons;

import java.util.Random;

public class GameManager {
    private int life;
    private int lanes;
    private int max_obstacles;
    private int airBalloonIndex;
    private boolean[][] matObstacles;
    private boolean[] arrAirBalloons;
    private int collisionsNum;
    private final Random rand;
    private int timerLaps;


    public GameManager() {
        this(3,3,6);
    }

    public GameManager(int life, int lanes, int max_obstacles){
        this.life = life;
        this.airBalloonIndex = 1;
        this.lanes = lanes;
        this.max_obstacles = max_obstacles;
        this.collisionsNum = 0;
        this.timerLaps = 0;
        rand = new Random();
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

    public GameManager setMatObstacles(boolean[][] matObstacles) {
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

    private void initObstacles(){
        this.matObstacles = new boolean[lanes][max_obstacles];
        for(int i = 0; i < lanes; i++)
            for(int j = 0; j < max_obstacles; j++)
                matObstacles[i][j] = false;
    }

    public boolean[][] getMatObstacles() {
        return matObstacles;
    }

    public void updateObstacles(){
        for(int i = 0; i < lanes; i++) {
            for(int j = max_obstacles-1; j > 0; j--) {
                if(j == max_obstacles - 1 && matObstacles[i][j]) {
                    checkCollision();
                    if(matObstacles[i][j-1])
                        matObstacles[i][j - 1] = false;
                    else
                        matObstacles[i][j] = false;
                }
                else if(matObstacles[i][j-1]) {
                    matObstacles[i][j - 1] = false;
                    matObstacles[i][j] = true;
                }
            }
        }
        /* Add an obstacle every two timer intervals */
        if(timerLaps % 2 == 0) {
            int rnd_col = rand.nextInt(lanes);
            matObstacles[rnd_col][0] = true;
        }
        timerLaps++;
    }

    private void initAirBalloons() {
        this.arrAirBalloons =  new boolean[lanes];
        updateAirBalloons();
    }

    public boolean[] getAirBalloons() {
        return arrAirBalloons;
    }

    public GameManager setArrAirBalloons(boolean[] arrAirBalloons) {
        this.arrAirBalloons = arrAirBalloons;
        return this;
    }

    private void updateAirBalloons(){
        for(int i = 0; i < lanes; i++)
        {
            if(i != airBalloonIndex)
                arrAirBalloons[i] = false;
            else arrAirBalloons[i] = true;
        }
    }

    public void moveAirBalloonRight(){
        if(airBalloonIndex < lanes - 1) {
            airBalloonIndex++;
            updateAirBalloons();
        }
    }

    public void moveAirBalloonLeft(){
        if(airBalloonIndex > 0) {
            airBalloonIndex--;
            updateAirBalloons();
        }
    }

    private void checkCollision(){
        if(matObstacles[airBalloonIndex][max_obstacles-1] && collisionsNum < life){
            this.collisionsNum++;
            SignalManager.getInstance().vibrate(200);
            SignalManager.getInstance().toast("Oops");
        }
    }

    public int getCollisionsNum() {
        return collisionsNum;
    }

    public GameManager setCollisionsNum(int collisionsNum) {
        this.collisionsNum = collisionsNum;
        return this;
    }
}

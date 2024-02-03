package com.example.hw1_airballoons;
import java.util.ArrayList;
public class GameManager {

    private int life;
    private int lanes;
    private int airballoonIndex;


    //private ArrayList<Obstacle> allObstacles;

    public GameManager(int life, int lanes){
        this.life = life;
        this.airballoonIndex = 1;
        this.lanes = lanes;
    }

    public int getAirballoonIndex() {
        return airballoonIndex;
    }

    public GameManager setAirballoonIndex(int newIndex) {
        this.airballoonIndex = newIndex;
        return this;
    }


}

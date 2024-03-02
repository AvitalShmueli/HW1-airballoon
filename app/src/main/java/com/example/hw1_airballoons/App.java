package com.example.hw1_airballoons;

import android.app.Application;

import com.example.hw1_airballoons.Utilities.SharedPreferencesManager;
import com.example.hw1_airballoons.Utilities.SignalManager;

public class App extends Application {

    public static final double DEFAULT_LAN = 32.113442;
    public static final double DEFAULT_LON = 34.818028;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
        SignalManager.init(this);
    }
}

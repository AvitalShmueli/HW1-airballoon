package com.example.hw1_airballoons;

import android.app.Application;

import com.example.hw1_airballoons.Utilities.SharedPreferencesManager;
import com.example.hw1_airballoons.Utilities.SignalManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
        SignalManager.init(this);
    }
}

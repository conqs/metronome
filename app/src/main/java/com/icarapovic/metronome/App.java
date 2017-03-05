package com.icarapovic.metronome;

import android.app.Application;

import com.icarapovic.metronome.utils.MediaUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MediaUtils.initController(this);
    }
}

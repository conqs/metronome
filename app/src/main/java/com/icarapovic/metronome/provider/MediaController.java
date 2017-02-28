package com.icarapovic.metronome.provider;

public interface MediaController {

    int REPEAT_OFF = 0;
    int REPEAT_ONE = 1;
    int REPEAT_ALL = 2;
    int SHUFFLE_OFF = 3;
    int SHUFFLE_ON = 4;

    // query service/playback status
    boolean isPlaying();

    int getShuffleMode();
    int getRepeatMode();

    // control playback
    void play();
    void pause();
    void next();
    void previous();
    void toggleShuffle();
    void toggleRepeat();
}

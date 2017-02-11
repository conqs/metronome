package com.icarapovic.metronome.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.icarapovic.metronome.utils.Command.ENQUEUE;
import static com.icarapovic.metronome.utils.Command.FAVORITE;
import static com.icarapovic.metronome.utils.Command.INIT;
import static com.icarapovic.metronome.utils.Command.NEXT;
import static com.icarapovic.metronome.utils.Command.PLAY_PAUSE;
import static com.icarapovic.metronome.utils.Command.PREVIOUS;
import static com.icarapovic.metronome.utils.Command.REPEAT;
import static com.icarapovic.metronome.utils.Command.SHUFFLE;

@Retention(RetentionPolicy.SOURCE)
@IntDef(value = {
        INIT,
        PLAY_PAUSE,
        NEXT,
        PREVIOUS,
        SHUFFLE,
        REPEAT,
        FAVORITE,
        ENQUEUE})
public @interface Command {
    int INIT = 0;
    int PLAY_PAUSE = 1;
    int NEXT = 2;
    int PREVIOUS = 3;
    int SHUFFLE = 4;
    int REPEAT = 5;
    int FAVORITE = 6;
    int ENQUEUE = 7;
}

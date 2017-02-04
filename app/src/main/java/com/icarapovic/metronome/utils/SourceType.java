package com.icarapovic.metronome.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.icarapovic.metronome.utils.SourceType.ALBUM;
import static com.icarapovic.metronome.utils.SourceType.ARTIST;
import static com.icarapovic.metronome.utils.SourceType.GENRE;
import static com.icarapovic.metronome.utils.SourceType.PLAYLIST;
import static com.icarapovic.metronome.utils.SourceType.SONG;

@Retention(RetentionPolicy.SOURCE)
@IntDef(value = {SONG, ALBUM, ARTIST, GENRE, PLAYLIST})
public @interface SourceType {
    int SONG = 1;
    int ALBUM = 2;
    int ARTIST = 3;
    int GENRE = 4;
    int PLAYLIST = 5;
}

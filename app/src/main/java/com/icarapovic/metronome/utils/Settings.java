package com.icarapovic.metronome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.icarapovic.metronome.service.MediaController;

public class Settings {

    // -----  SETTING KEYS -------
    private static final String SETTINGS = "com.icarapovic.metronome.settings.SETTINGS";
    private static final String PREF_SHUFFLE = "com.icarapovic.metronome.settings.PREF_SHUFFLE";
    private static final String PREF_REPEAT = "com.icarapovic.metronome.settings.PREF_REPEAT";
    private static final String PREF_SHOW_ARTWORK_IN_SONGS = "com.icarapovic.metronome.settings.PREF_SHOW_ARTWORK_IN_SONGS";

    // -----  HELPER METHODS -------

    /**
     * Returns instance of this apps SharedPreferences
     */
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    /**
     * Returns instance of this apps SharedPreferences editor
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return getPrefs(context).edit();
    }

    // -----  MEDIA SETTINGS -------

    /**
     * Toggles shuffle mode ON or OFF
     */
    public static void toggleShuffleMode(Context context) {
        int state = getPrefs(context).getInt(PREF_SHUFFLE, MediaController.SHUFFLE_OFF);
        if (state == MediaController.SHUFFLE_OFF) {
            getEditor(context).putInt(PREF_SHUFFLE, MediaController.SHUFFLE_ON).apply();
        } else {
            getEditor(context).putInt(PREF_SHUFFLE, MediaController.SHUFFLE_OFF).apply();
        }
    }

    /**
     * Read current shuffle state from preferences
     */
    public static int getShuffleMode(Context context) {
        return getPrefs(context).getInt(PREF_SHUFFLE, MediaController.SHUFFLE_OFF);
    }

    /**
     * Toggles repeat mode to OFF, ONE or ALL
     */
    public static void toggleRepeatMode(Context context) {
        int state = getPrefs(context).getInt(PREF_REPEAT, MediaController.REPEAT_OFF);
        if (state == MediaController.REPEAT_OFF) {
            getEditor(context).putInt(PREF_REPEAT, MediaController.REPEAT_ONE).apply();
        } else if (state == MediaController.REPEAT_ONE) {
            getEditor(context).putInt(PREF_REPEAT, MediaController.REPEAT_ALL).apply();
        } else {
            getEditor(context).putInt(PREF_REPEAT, MediaController.REPEAT_OFF).apply();
        }
    }

    /**
     * Read current repeat mode from preferences
     */
    public static int getRepeatMode(Context context) {
        return getPrefs(context).getInt(PREF_REPEAT, MediaController.REPEAT_OFF);
    }

    // ----- UI SETTINGS ---------

    /**
     * Sets album artwork in song list on or off
     */
    public static void setShowArtworkInSongList(Context context, boolean showArtwork) {
        getEditor(context).putBoolean(PREF_SHOW_ARTWORK_IN_SONGS, showArtwork).apply();
    }

    /**
     * Returns true if songs list should show album artwork, false otherwise
     */
    public static boolean getShouldShowArtworkInSongsList(Context context) {
        return getPrefs(context).getBoolean(PREF_SHOW_ARTWORK_IN_SONGS, true);
    }
}

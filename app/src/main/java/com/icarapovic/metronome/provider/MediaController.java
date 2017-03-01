package com.icarapovic.metronome.provider;

import com.icarapovic.metronome.models.Song;

public interface MediaController {

    int REPEAT_OFF = 0;
    int REPEAT_ONE = 1;
    int REPEAT_ALL = 2;
    int SHUFFLE_OFF = 3;
    int SHUFFLE_ON = 4;

    /**
     * Returns true if music is playing, false otherwise
     */
    boolean isPlaying();

    /**
     * Returns the current shuffle mode
     *
     * @return MediaController.SHUFFLE_ON or SHUFFLE_OFF
     */
    int getShuffleMode();

    /**
     * Returns the current repeat mode
     * @return MediaController.REPEAT_OFF, REPEAT_ONE or REPEAT_ALL
     * */
    int getRepeatMode();

    /**
     * Start playback of the provided song
     *
     * @param song Song to be played
     */
    void play(Song song);

    /**
     * Start playback after it has been paused
     * */
    void play();

    /**
     * Pause the currently playing song.<br>
     * Does nothing if nothing is currently playing.
     * */
    void pause();

    /**
     * Skip to the next song on the list
     * The next song will depend on the shuffle and repeat states
     * */
    void next();

    /**
     * Skip to the previous song on the list<br>
     * The next song will depend on the shuffle and repeat states
     * */
    void previous();

    /**
     * Change shuffle mode<br>
     * The shuffle mode will be turned on if it was off and vice versa
     * */
    void toggleShuffle();

    /**
     * Change repeat mode<br><br>
     * The repeat will change in following order:<br>
     * OFF -> ONE -> ALL -> OFF
     * */
    void toggleRepeat();
}

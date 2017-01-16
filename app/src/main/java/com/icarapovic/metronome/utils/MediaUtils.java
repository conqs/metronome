package com.icarapovic.metronome.utils;


public class MediaUtils {

    private MediaUtils(){}

    private static final String DELIMITER = ":";

    /**
     * Get string representation of media duration
     *
     * @param duration the duration of the item in milliseconds
     * @return Duration in HH:mm:ss representation
     * */
    public static String formatDuration(long duration){
        StringBuilder sb = new StringBuilder();

        // total hours
        int hours = (int) (duration / 1000 / 60 / 60);
        // total minutes - minutes in the hours
        int minutes = (int) (duration / 1000 / 60) - (hours * 60);
        // total seconds - seconds in hours - seconds in minutes
        int seconds = (int) (duration / 1000) - (hours * 60 * 60) - (minutes * 60);

        if(hours != 0){
            if(hours < 9){
               sb.append("0");
            }

            sb.append(hours).append(DELIMITER);
        }

        if(minutes < 9){
            sb.append("0");
        }

        sb.append(minutes).append(DELIMITER);

        if(seconds < 9){
            sb.append("0");
        }

        sb.append(seconds);

        return sb.toString();
    }
}

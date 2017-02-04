package com.icarapovic.metronome.utils;

import android.content.ContentUris;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.icarapovic.metronome.R;

public class MediaUtils {

    private MediaUtils(){}

    private static final String DELIMITER = ":";
    private static final float SIZE_MULTIPLIER = 0.3f;

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
            sb.append(hours).append(DELIMITER);
        }

        if (minutes < 10) {
            sb.append("0");
        }

        sb.append(minutes).append(DELIMITER);

        if (seconds < 10) {
            sb.append("0");
        }

        sb.append(seconds);

        return sb.toString();
    }

    /**
     * Load album art from MediaStore into an ImageView
     *
     * @param songId ID of the song
     * @param view The view to display album artwork
     */
    public static void loadSongArt(int songId, ImageView view) {
        Uri artworkUri = Uri.parse("content://media/external/audio/media/" + songId + "/albumart");
        Glide.with(view.getContext())
                .loadFromMediaStore(artworkUri)
                .error(R.drawable.ic_album)
                .into(view);
    }

    public static void loadAlbumArt(int albumId, ImageView view) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
        Glide.with(view.getContext())
                .loadFromMediaStore(uri)
                .error(R.drawable.ic_album)
                .sizeMultiplier(SIZE_MULTIPLIER)
                .into(view);
    }
}


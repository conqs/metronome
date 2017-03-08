package com.icarapovic.metronome.utils;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.icarapovic.metronome.R;
import com.icarapovic.metronome.service.MediaController;
import com.icarapovic.metronome.service.MediaService;

public class MediaUtils {

    private static final String DELIMITER = ":";

    private static ServiceConnection serviceConnection;
    private static MediaController mediaController;
    private static boolean isBound = false;

    private MediaUtils() {
    }

    /**
     * Initialize the MediaController object
     **/
    public static void initController(Context context) {
        initServiceConnection();
        Intent i = new Intent(context, MediaService.class);
        context.startService(i);
        context.bindService(i, serviceConnection, 0);
    }

    public static MediaController getMediaController() {
        if (!isBound) {
            throw new IllegalStateException("MediaController is NULL, did you forget to init?");
        }

        return mediaController;
    }

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
    public static void loadSongArt(int songId, ImageView view, float quality) {
        Uri artworkUri = Uri.parse("content://media/external/audio/media/" + songId + "/albumart");
        Glide.with(view.getContext())
                .loadFromMediaStore(artworkUri)
                .placeholder(R.drawable.ic_album)
                .error(R.drawable.ic_album)
                .sizeMultiplier(quality)
                .into(view);
    }

    /**
     * Load album art from MediaStore into an ImageView
     *
     * @param albumId ID of the album
     * @param view    The view to display album artwork
     */
    public static void loadAlbumArt(int albumId, ImageView view, float quality) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
        Glide.with(view.getContext())
                .loadFromMediaStore(uri)
                .error(R.drawable.ic_album)
                .sizeMultiplier(quality)
                .dontAnimate()
                .into(view);
    }

    private static void initServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mediaController = ((MediaService.LocalBinder) service).getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mediaController = null;
                isBound = false;
            }
        };
    }
}


package com.icarapovic.metronome.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.ui.activities.NowPlayingActivity;
import com.icarapovic.metronome.utils.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaService extends Service implements
        AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaController {

    public static final String ACTION_SHUTDOWN = "com.icarapovic.service.ACTION_SHUTDOWN";
    public static final String ACTION_SYNC_STATE = "com.icarapovic.service.ACTION_SYNC_STATE";

    private static final int NOTIFICATION_ID = 2905992;
    private static final float VOLUME_MAX = 1.0f;
    private static final float VOLUME_DUCKED = 0.3f;
    private static final String TAG = "MediaService";

    private MediaPlayer mediaPlayer;
    private BroadcastReceiver headphonesReceiver;
    private AudioManager audioManager;
    private Song currentSong;
    private List<Song> queue;
    private IBinder localBinder;
    private Notification notification;
    private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        localBinder = new LocalBinder();
        queue = new ArrayList<>();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        setupMediaPlayer();
        initNoisyReceiver();
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();

        // prevent CPU to go into deep sleep to allow music playback while screen is off
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);

        // don't even start the service if we can't get audio focus
        if (!requestAudioFocus()) {
            stopSelf();
        }
    }

    private void initNoisyReceiver() {
        headphonesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        };

        // AUDIO_BECOMING_NOISY
        // Headphones get unplugged while music is playing, phone would play on speaker and therefore become noisy
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(headphonesReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_SHUTDOWN)) {
                stopSelf();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            // Audio focus lost to other app, stop playback
            case AudioManager.AUDIOFOCUS_LOSS: {
                if (isPlaying()) {
                    mediaPlayer.stop();
                    stopForeground(false);
                }
                break;
            }
            // Audio focus temporary lost, pause playback, will continue soon
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                pause();
                break;
            }
            // Audio focus temporary lost, continue playback with low volume
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(VOLUME_DUCKED, VOLUME_DUCKED);
                }
                break;
            }
            // We got audio focus, can start playing or restore full volume
            case AudioManager.AUDIOFOCUS_GAIN: {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
                }
                break;
            }
        }
    }

    private boolean requestAudioFocus() {
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_GAIN;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        int position = queue.indexOf(currentSong);
        switch (getRepeatMode()) {
            case REPEAT_OFF:
                if (position < queue.size() - 1) {
                    play(queue.get(++position));
                } else {
                    mediaPlayer.reset();
                    stopForeground(false);
                }
                break;
            case REPEAT_ONE:
                mediaPlayer.reset();
                play(currentSong);
                break;
            case REPEAT_ALL:
                if (position == queue.size() - 1) {
                    play(queue.get(0));
                } else {
                    next();
                }
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        showNotification();
        broadcastManager.sendBroadcast(createSyncIntent());
    }

    private Intent createSyncIntent() {
        return new Intent(ACTION_SYNC_STATE);
    }



    @Override
    public void onDestroy() {
        mediaPlayer.release();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        unregisterReceiver(headphonesReceiver);
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean isPlaying() {
        try {
            return mediaPlayer.isPlaying();
        } catch (NullPointerException | IllegalStateException ex) {
            // if mediaPlayer is null, or in an invalid state, its surely not playing
            return false;
        }
    }

    @Override
    public int getShuffleMode() {
        return Settings.getShuffleMode(this);
    }

    @Override
    public int getRepeatMode() {
        return Settings.getRepeatMode(this);
    }

    @Override
    public Song getActiveSong() {
        return currentSong != null ? currentSong : new Song.Builder().setId(-1).build();
    }

    @Override
    public void play(Song song, List<Song> queue) {
        if (!queue.contains(song)) {
            throw new IllegalArgumentException("Song does not belong to the queue!");
        }

        this.queue = queue;
        play(song);
    }

    @Override
    public void play(Song song) {
        currentSong = song;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.reset();
        }
    }

    @Override
    public void play() {
        if (!isPlaying()) {
            mediaPlayer.start();
            showNotification();
        }
    }

    @Override
    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
            stopForeground(false);
        }
    }

    @Override
    public void next() {
        if (queue.contains(currentSong)) {
            int index = queue.indexOf(currentSong);
            currentSong = index == (queue.size() - 1) ? queue.get(0) : queue.get(++index);
            play(currentSong);
        }
    }

    @Override
    public void previous() {
        if (queue.contains(currentSong)) {
            int index = queue.indexOf(currentSong);
            currentSong = index == 0 ? queue.get(0) : queue.get(--index);
            play(currentSong);
        }
    }

    @Override
    public void toggleShuffle() {
        Settings.toggleShuffleMode(this);
    }

    @Override
    public void toggleRepeat() {
        Settings.toggleRepeatMode(this);
    }

    @Override
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    @Override
    public int getCurrentPlaybackPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public List<Song> getQueue() {
        return queue;
    }

    public void showNotification() {
        startForeground(NOTIFICATION_ID, getNotification());
    }

    private Notification getNotification() {
        if (notification != null) {
            return notification;
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Metronome")
                    .setContentText("Music is playing")
                    .setSmallIcon(R.drawable.ic_play)
                    .setContentIntent(createNowPlayingIntent())
                    .build();
        }

        return notification;
    }

    private PendingIntent createNowPlayingIntent() {
        Intent intent = new Intent(this, NowPlayingActivity.class);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public class LocalBinder extends Binder {
        public MediaController getService() {
            return MediaService.this;
        }
    }
}

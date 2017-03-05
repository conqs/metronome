package com.icarapovic.metronome.service;

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
import android.support.v4.app.NotificationManagerCompat;

import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.provider.MediaController;
import com.icarapovic.metronome.utils.Settings;

import java.io.IOException;
import java.util.List;

public class MediaService extends Service implements
        AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaController {

    private static final int NOTIFICATION_ID = 2905992;
    private static final float VOLUME_MAX = 1.0f;
    private static final float VOLUME_DUCKED = 0.3f;
    private static final String TAG = "MediaService";
    private static final String COMMAND_SHUTDOWN = "com.icarapovic.command.COMMAND_SHUTDOWN";
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver headphonesReceiver;
    private int queuePosition = -1;
    private List<Song> queue;
    private AudioManager audioManager;
    private IBinder localBinder;

    @Override
    public void onCreate() {
        super.onCreate();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        localBinder = new LocalBinder();
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
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
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
            if (intent.getAction().equals(COMMAND_SHUTDOWN)) {
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
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                break;
            }
            // Audio focus temporary lost, pause playback, will continue soon
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                mediaPlayer.pause();
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
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
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
        // TODO
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        unregisterReceiver(headphonesReceiver);
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
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
            // if mediaPlayer is null, or in an invalid state, its definitely not playing
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
    public void play(Song song) {
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
        }
    }

    @Override
    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void next() {
        // TODO
    }

    @Override
    public void previous() {
        // TODO
    }

    @Override
    public void toggleShuffle() {
        Settings.toggleShuffleMode(this);
    }

    @Override
    public void toggleRepeat() {
        Settings.toggleRepeatMode(this);
    }

    public class LocalBinder extends Binder {
        public MediaController getService() {
            return MediaService.this;
        }
    }
}

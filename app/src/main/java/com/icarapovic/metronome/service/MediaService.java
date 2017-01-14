package com.icarapovic.metronome.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

public class MediaService extends Service implements
        AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener {

    private static final int NOTIFICATION_ID = 2905992;
    private static final float VOLUME_MAX = 1.0f;
    private static final float VOLUME_DUCKED = 0.3f;
    private static final String TAG = "MediaService";

    private MediaPlayer mMediaPlayer;
    private BroadcastReceiver mHeadphonesReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        initMediaPlayer();
        initNoisyReceiver();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();

        // prevent CPU to go into deep sleep to allow music playback while screen is off
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
    }

    private void initNoisyReceiver() {
        mHeadphonesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
            }
        };

        // AUDIO_BECOMING_NOISY
        // Headphones get unplugged while music is playing, phone would play on speaker and therefore become noisy
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mHeadphonesReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Intent is sent which has an Action (Intent.getAction()) according to which some command is to be executed
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            // Audio focus lost to other app, stop playback
            case AudioManager.AUDIOFOCUS_LOSS: {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                break;
            }
            // Audio focus temporary lost, pause playback, will continue soon
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                mMediaPlayer.pause();
                break;
            }
            // Audio focus temporary lost, continue playback with low volume
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(VOLUME_DUCKED, VOLUME_DUCKED);
                }
                break;
            }
            // We got audio focus, can start playing or restore full volume
            case AudioManager.AUDIOFOCUS_GAIN: {
                if (mMediaPlayer != null) {
                    if (!mMediaPlayer.isPlaying()) {
                        mMediaPlayer.start();
                    }
                    mMediaPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
                }
                break;
            }
        }
    }

    private boolean hasAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        return result == AudioManager.AUDIOFOCUS_GAIN;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        unregisterReceiver(mHeadphonesReceiver);
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

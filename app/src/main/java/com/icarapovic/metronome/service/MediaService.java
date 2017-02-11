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

import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.utils.Command;

import java.io.IOException;
import java.util.List;

public class MediaService extends Service implements
        AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    private static final int NOTIFICATION_ID = 2905992;
    private static final float VOLUME_MAX = 1.0f;
    private static final float VOLUME_DUCKED = 0.3f;
    private static final String TAG = "MediaService";
    public static final String COMMAND = "com.icarapovic.action.MEDIA_SERVICE_COMMAND";
    public static final String EXTRA_TRACK = "com.icarapovic.data.DATA";
    public static final String EXTRA_TRACK_LIST = "com.icarapovic.data.DATA_LIST";

    private MediaPlayer mMediaPlayer;
    private BroadcastReceiver mHeadphonesReceiver;
    private int mQueuePosition = -1;
    private List<Song> mQueue;
    private AudioManager mAudioManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setupMediaPlayer();
        initNoisyReceiver();
    }

    private void setupMediaPlayer() {
        mMediaPlayer = new MediaPlayer();

        // prevent CPU to go into deep sleep to allow music playback while screen is off
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);

        // don't even start the service if we can't get audio focus
        if (!requestAudioFocus()) {
            stopSelf();
        }
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
        int command = intent.getIntExtra(COMMAND, -1);
        if (command == -1) {
            throw new IllegalArgumentException("Service command invalid!");
        }

        handleCommand(intent);

        return START_STICKY;
    }

    private void handleCommand(Intent intent) {
        Song song = intent.getParcelableExtra(EXTRA_TRACK);
        List<Song> queue = intent.getParcelableArrayListExtra(EXTRA_TRACK_LIST);

        switch (intent.getIntExtra(COMMAND, -1)) {
            case Command.PLAY_PAUSE:
                try {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mAudioManager.abandonAudioFocus(this);
                    } else {
                        if (requestAudioFocus()) {
                            mMediaPlayer.start();
                        }
                    }
                } catch (IllegalStateException ex) {
                    initMediaPlayer(song, queue);
                }
                break;
            case Command.NEXT:
                break;
            case Command.PREVIOUS:
                break;
            case Command.REPEAT:
                break;
            case Command.SHUFFLE:
                break;
            case Command.FAVORITE:
                break;
            case Command.ENQUEUE:
                break;
            case Command.INIT:
                initMediaPlayer(song, queue);
                break;
            default:
                // invalid call, ideally this should never occur
                stopSelf();
        }
    }

    private void initMediaPlayer(Song song, List<Song> queue) {
        if (song != null) {
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(song.getPath());
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (queue != null) {
                mQueue = queue;
            }
        } else {
            throw new IllegalArgumentException("song = null, mediaPlayer not initialized!");
        }
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

    private boolean requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
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
        mMediaPlayer.release();
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

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
import android.widget.SeekBar;

import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.provider.MediaController;
import com.icarapovic.metronome.utils.Settings;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MediaService extends Service implements
        AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaController {

    private static final int SEEK_BAR_FPS = 30;
    private static final int NOTIFICATION_ID = 2905992;
    private static final float VOLUME_MAX = 1.0f;
    private static final float VOLUME_DUCKED = 0.3f;
    private static final String TAG = "MediaService";
    private static final String COMMAND_SHUTDOWN = "com.icarapovic.command.COMMAND_SHUTDOWN";
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver headphonesReceiver;
    private AudioManager audioManager;
    private Song currentSong;
    private List<Song> queue;
    private IBinder localBinder;
    private WeakReference<SeekBar> seekBar;
    private ScheduledExecutorService scheduledExecutor;
    private ScheduledFuture<?> seekBarUpdateTask;

    @Override
    public void onCreate() {
        super.onCreate();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        localBinder = new LocalBinder();
        queue = new ArrayList<>();
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
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
                if (isPlaying()) {
                    mediaPlayer.stop();
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
                }
                break;
            case REPEAT_ONE:
                mediaPlayer.reset();
                play(currentSong);
                break;
            case REPEAT_ALL:
                if (position < queue.size() - 1) {
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
    }

    private void startSeekBarUpdates() {
        if (seekBarUpdateTask != null) {
            seekBarUpdateTask.cancel(true);
        }

        seekBar.get().setMax((int) getActiveSong().getDuration());
        seekBarUpdateTask = scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                seekBar.get().setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000 / SEEK_BAR_FPS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDestroy() {
        seekBarUpdateTask.cancel(true);
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
        if (seekBarUpdateTask != null) {
            seekBarUpdateTask.cancel(true);
            startSeekBarUpdates();
        }

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
            startSeekBarUpdates();
        }
    }

    @Override
    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
            seekBarUpdateTask.cancel(true);
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
    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = new WeakReference<>(seekBar);
        this.seekBar.get().setOnSeekBarChangeListener(this);
        if (isPlaying()) {
            startSeekBarUpdates();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.start();
    }

    public class LocalBinder extends Binder {
        public MediaController getService() {
            return MediaService.this;
        }
    }
}

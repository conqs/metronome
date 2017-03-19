package com.icarapovic.metronome.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.service.MediaController;
import com.icarapovic.metronome.service.MediaService;
import com.icarapovic.metronome.utils.MediaUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NowPlayingActivity extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener {

    private static final int SEEK_BAR_FPS = 30;

    @BindView(R.id.album_art)
    ImageView albumArt;
    @BindView(R.id.play_pause)
    ImageView playPause;
    @BindView(R.id.shuffle)
    ImageView shuffle;
    @BindView(R.id.repeat)
    ImageView repeat;
    @BindView(R.id.seeker)
    SeekBar seekBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MediaController controller;
    private BroadcastReceiver syncListener;
    private ScheduledExecutorService scheduledExecutor;
    private ScheduledFuture<?> seekBarUpdateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        controller = MediaUtils.getMediaController();
        seekBar.setOnSeekBarChangeListener(this);
        syncListener = createSyncListener();
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private BroadcastReceiver createSyncListener() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MediaService.ACTION_SYNC_STATE)) {
                    onPlaybackStateChanged();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(syncListener, new IntentFilter(MediaService.ACTION_SYNC_STATE));
        onPlaybackStateChanged();
        startSeekBarUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(syncListener);
        seekBarUpdateTask.cancel(true);
    }

    private void loadArtwork() {
        MediaUtils.loadAlbumArt(controller.getActiveSong().getAlbumId(), albumArt, 1.0f);
    }

    private void syncShuffleIcon() {
        switch (controller.getShuffleMode()) {
            case MediaController.SHUFFLE_OFF:
                shuffle.setImageResource(R.drawable.ic_shuffle_off);
                break;
            case MediaController.SHUFFLE_ON:
                shuffle.setImageResource(R.drawable.ic_shuffle_on);
                break;
        }
    }

    private void syncRepeatIcon() {
        switch (controller.getRepeatMode()) {
            case MediaController.REPEAT_OFF:
                repeat.setImageResource(R.drawable.ic_repeat);
                repeat.setAlpha(0.3f);
                break;
            case MediaController.REPEAT_ONE:
                repeat.setImageResource(R.drawable.ic_repeat_one);
                repeat.setAlpha(1f);
                break;
            case MediaController.REPEAT_ALL:
                repeat.setImageResource(R.drawable.ic_repeat);
                repeat.setAlpha(1f);
                break;
        }
    }

    @OnClick(R.id.play_pause)
    public void playPause() {
        if (controller.isPlaying()) {
            controller.pause();
        } else {
            controller.play();
        }

        onPlaybackStateChanged();
    }

    @OnClick(R.id.previous)
    public void previous() {
        controller.previous();
        loadArtwork();
    }

    @OnClick(R.id.next)
    public void next() {
        controller.next();
        loadArtwork();
    }

    @OnClick(R.id.repeat)
    public void repeat() {
        controller.toggleRepeat();
        syncRepeatIcon();
    }

    @OnClick(R.id.shuffle)
    public void shuffle() {
        controller.toggleShuffle();
        syncShuffleIcon();
    }

    public void onPlaybackStateChanged() {
        playPause.setImageResource(controller.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
        loadArtwork();
        syncRepeatIcon();
        syncShuffleIcon();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            controller.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBarUpdateTask.cancel(true);
        controller.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        controller.play();
        startSeekBarUpdates();
    }

    private void startSeekBarUpdates() {
        if (seekBarUpdateTask != null) {
            seekBarUpdateTask.cancel(true);
        }

        seekBar.setMax((int) controller.getActiveSong().getDuration());
        seekBarUpdateTask = scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(controller.getCurrentPlaybackPosition());
            }
        }, 0, 1000 / SEEK_BAR_FPS, TimeUnit.MILLISECONDS);
    }
}

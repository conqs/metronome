package com.icarapovic.metronome.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.service.MediaController;
import com.icarapovic.metronome.service.PlaybackListener;
import com.icarapovic.metronome.utils.MediaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NowPlayingActivity extends AppCompatActivity implements
        PlaybackListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);

        controller = MediaUtils.getMediaController();
        controller.setSeekBar(seekBar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onPlaybackStateChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.addPlaybackStateListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.removePlaybackStateListener(this);
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

    @Override
    public void onPlaybackStateChanged() {
        playPause.setImageResource(controller.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
        loadArtwork();
        syncRepeatIcon();
        syncShuffleIcon();
    }
}

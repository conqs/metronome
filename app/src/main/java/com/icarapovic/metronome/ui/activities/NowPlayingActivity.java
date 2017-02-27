package com.icarapovic.metronome.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.provider.MediaController;
import com.icarapovic.metronome.service.MediaService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NowPlayingActivity extends AppCompatActivity {

    @BindView(R.id.album_art)
    ImageView albumArt;
    @BindView(R.id.play_pause)
    ImageView playPause;
    @BindView(R.id.shuffle)
    ImageView shuffle;
    @BindView(R.id.repeat)
    ImageView repeat;

    MediaController mediaController;
    boolean isBound = false;
    ServiceConnection connection = new ServiceConnection() {
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
    private ArrayList<Song> mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);

        bindService(new Intent(this, MediaService.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO check if service bound, handle worst case
    }

    /**
     * Sync the UI with the playback state
     */
    private void syncState() {
        if (isBound) {
            playPause.setImageResource(mediaController.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
            shuffle.setImageResource(mediaController.isShuffling() ? R.drawable.ic_shuffle_on : R.drawable.ic_shuffle_off);
            syncRepeatIcon();
        }
    }

    private void syncRepeatIcon() {
        switch (mediaController.getRepeatMode()) {
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
        if (mediaController.isPlaying()) {
            mediaController.pause();
        } else {
            mediaController.play();
        }

        syncState();
    }

    @OnClick(R.id.previous)
    public void previous() {
        mediaController.previous();
    }

    @OnClick(R.id.next)
    public void next() {
        mediaController.next();
    }

    @OnClick(R.id.repeat)
    public void repeat() {
        mediaController.toggleRepeat();
        syncRepeatIcon();
    }

    @OnClick(R.id.shuffle)
    public void shuffle() {
        mediaController.toggleShuffle();
        syncState();
    }
}

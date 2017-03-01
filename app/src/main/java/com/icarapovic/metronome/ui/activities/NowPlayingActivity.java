package com.icarapovic.metronome.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.provider.MediaController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NowPlayingActivity extends BaseActivity {

    @BindView(R.id.album_art)
    ImageView albumArt;
    @BindView(R.id.play_pause)
    ImageView playPause;
    @BindView(R.id.shuffle)
    ImageView shuffle;
    @BindView(R.id.repeat)
    ImageView repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncState();
        // TODO check if service bound, handle worst case
    }

    /**
     * Sync the UI with the playback state
     */
    private void syncState() {
        if (isServiceBound()) {
            playPause.setImageResource(getController().isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
            syncRepeatIcon();
            syncShuffleIcon();
        }
    }

    private void syncShuffleIcon() {
        switch (getController().getShuffleMode()) {
            case MediaController.SHUFFLE_OFF:
                shuffle.setImageResource(R.drawable.ic_shuffle_off);
                break;
            case MediaController.SHUFFLE_ON:
                shuffle.setImageResource(R.drawable.ic_shuffle_on);
                break;
        }
    }

    private void syncRepeatIcon() {
        switch (getController().getRepeatMode()) {
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
        if (getController().isPlaying()) {
            getController().pause();
        } else {
            getController().play();
        }

        syncState();
    }

    @OnClick(R.id.previous)
    public void previous() {
        getController().previous();
    }

    @OnClick(R.id.next)
    public void next() {
        getController().next();
    }

    @OnClick(R.id.repeat)
    public void repeat() {
        getController().toggleRepeat();
        syncRepeatIcon();
    }

    @OnClick(R.id.shuffle)
    public void shuffle() {
        getController().toggleShuffle();
        syncShuffleIcon();
    }
}

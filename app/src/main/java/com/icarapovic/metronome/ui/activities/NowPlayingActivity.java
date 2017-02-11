package com.icarapovic.metronome.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.service.MediaService;
import com.icarapovic.metronome.utils.Command;
import com.icarapovic.metronome.utils.MediaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NowPlayingActivity extends AppCompatActivity {

    @BindView(R.id.album_art)
    ImageView mAlbumArt;

    private Intent mCommand;
    private Song mCurrentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);

        mCommand = new Intent(this, MediaService.class);
        Intent i = getIntent();
        mCurrentSong = i.getParcelableExtra(MediaService.EXTRA_TRACK);
        MediaUtils.loadSongArt(mCurrentSong.getId(), mAlbumArt, 1f);
    }

    @OnClick(R.id.play_pause)
    public void playPause() {
        mCommand.putExtra(MediaService.COMMAND, Command.PLAY_PAUSE);
        startService(mCommand);
    }

}

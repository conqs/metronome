package com.icarapovic.metronome.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NowPlayingActivity extends AppCompatActivity {

    @BindView(R.id.album_art)
    ImageView mAlbumArt;

    private ArrayList<Song> mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.play_pause)
    public void playPause() {

    }

    @OnClick(R.id.previous)
    public void previous() {

    }

    @OnClick(R.id.next)
    public void next() {

    }

}

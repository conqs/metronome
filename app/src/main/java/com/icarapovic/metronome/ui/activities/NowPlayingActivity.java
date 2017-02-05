package com.icarapovic.metronome.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.utils.MediaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NowPlayingActivity extends AppCompatActivity {
    public static final String EXTRA_SOURCE_TYPE = "_extra_source_type";
    public static final String EXTRA_MEDIA_ID = "_extra_song_id";

    @BindView(R.id.album_art)
    ImageView mAlbumArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);

        Intent i = getIntent();
        int mediaId = i.getIntExtra(EXTRA_MEDIA_ID, 0);
        MediaUtils.loadSongArt(mediaId, mAlbumArt, 1f);
    }
}

package com.icarapovic.metronome.activities;

import android.content.ComponentName;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.service.MediaService;

public class LibraryActivity extends AppCompatActivity {

    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private int currentState;
    private MediaBrowserCompat mMediaBrowserCompat;
    private MediaBrowserCompat.ConnectionCallback mConnectionCallback;
    private MediaControllerCompat mMediaControllerCompat;
    private MediaControllerCompat.Callback mControllerCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        init();

        // create the media browser and attempt connecting to the service
        mMediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this, MediaService.class),
                mConnectionCallback, getIntent().getExtras());
        mMediaBrowserCompat.connect();

    }

    // init requirements for service connection
    private void init() {

        mControllerCallback = new MediaControllerCompat.Callback() {
            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {
                // TODO - fill this and override other methods from callback that may be useful
            }
        };

        mConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {
            @Override
            public void onConnected() {
                try {
                    mMediaControllerCompat = new MediaControllerCompat(LibraryActivity.this, mMediaBrowserCompat.getSessionToken());
                    mMediaControllerCompat.registerCallback(mControllerCallback);

                    // TODO check what is the non-deprecated counterpart
                    // setSupportMediaController(mMediaControllerCompat);

                    Toast.makeText(LibraryActivity.this, "Service connected!", Toast.LENGTH_LONG).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnectionFailed() {
                Toast.makeText(LibraryActivity.this, "Service connection failed!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectionSuspended() {
                Toast.makeText(LibraryActivity.this, "Service disconnected!", Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onDestroy() {
        mMediaBrowserCompat.disconnect();
        super.onDestroy();
    }
}

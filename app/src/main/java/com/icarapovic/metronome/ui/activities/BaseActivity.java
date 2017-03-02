package com.icarapovic.metronome.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.icarapovic.metronome.provider.MediaController;
import com.icarapovic.metronome.service.MediaService;

public class BaseActivity extends AppCompatActivity {

    private ServiceConnection serviceConnection;
    private MediaController mediaController;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initServiceConnection();
        bindService(new Intent(this, MediaService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * Initialize a service connection to be used when
     * binding to the media playback service
     */
    private void initServiceConnection() {
        serviceConnection = new ServiceConnection() {
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
    }

    /**
     * Returns the MediaController to control and
     * get the state of the playback service
     */
    public MediaController getController() {
        return mediaController;
    }

    /**
     * Returns true if service connection exists, false otherwise
     */
    public boolean isServiceBound() {
        return isBound;
    }
}

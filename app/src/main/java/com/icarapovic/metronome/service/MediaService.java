package com.icarapovic.metronome.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.icarapovic.metronome.R;

import java.io.IOException;
import java.util.List;

public class MediaService extends MediaBrowserServiceCompat implements
        AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener {

    private static final int NOTIFICATION_ID = 2905992;
    private static final float VOLUME_MAX = 1.0f;
    private static final float VOLUME_DUCKED = 0.3f;
    private static final String TAG = "MediaService";

    private MediaSessionCompat mMediaSession;
    private MediaPlayer mMediaPlayer;
    private BroadcastReceiver mHeadphonesReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        initMediaSession();
        initMediaPlayer();
        initNoisyReceiver();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();

        // prevent CPU to go into deep sleep to allow music playback while screen is off
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
    }

    private void initMediaSession() {
        // gets called when media control hardware buttons get pressed (volume up, down etc)
        ComponentName mediaButtonReceiver = new ComponentName(getApplicationContext(), MediaButtonReceiver.class);

        mMediaSession = new MediaSessionCompat(getApplicationContext(), TAG, mediaButtonReceiver, null);
        mMediaSession.setCallback(mediaSessionCallback);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(this, MediaButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
        mMediaSession.setMediaButtonReceiver(pendingIntent);

        mMediaSession = new MediaSessionCompat(this, TAG);
        setSessionToken(mMediaSession.getSessionToken());
    }

    private void initNoisyReceiver() {
        mHeadphonesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
            }
        };

        // AUDIO_BECOMING_NOISY
        // Headphones get unplugged while music is playing, phone would play on speaker and therefore become noisy
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mHeadphonesReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Intent is sent which has an Action (Intent.getAction()) according to which some command is to be executed
        MediaButtonReceiver.handleIntent(mMediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        // if this app requests, return BrowserRoot object to browse media
        if (TextUtils.equals(clientPackageName, getPackageName())) {
            return new BrowserRoot(getString(R.string.app_name), null);
        }

        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        // TODO return items
        result.sendResult(null);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            // Audio focus lost to other app, stop playback
            case AudioManager.AUDIOFOCUS_LOSS: {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                break;
            }
            // Audio focus temporary lost, pause playback, will continue soon
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                mMediaPlayer.pause();
                break;
            }
            // Audio focus temporary lost, continue playback with low volume
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(VOLUME_DUCKED, VOLUME_DUCKED);
                }
                break;
            }
            // We got audio focus, can start playing or restore full volume
            case AudioManager.AUDIOFOCUS_GAIN: {
                if (mMediaPlayer != null) {
                    if (!mMediaPlayer.isPlaying()) {
                        mMediaPlayer.start();
                    }
                    mMediaPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
                }
                break;
            }
        }
    }

    private MediaSessionCompat.Callback mediaSessionCallback = new MediaSessionCompat.Callback() {

        @Override
        public void onPlay() {
            if (hasAudioFocus()) {
                // start a media session
                mMediaSession.setActive(true);
                setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING);

                // start playback and show ongoing notification
                buildNotification(PlaybackStateCompat.STATE_PLAYING);
                mMediaPlayer.start();
            }
        }

        @Override
        public void onPause() {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED);
                buildNotification(PlaybackStateCompat.STATE_PAUSED);
            }
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            try {
                mMediaPlayer.setDataSource(MediaService.this, uri);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void buildNotification(int state) {
        NotificationCompat.Builder builder = MediaStyleHelper.from(this, mMediaSession);
        if (builder != null) {
            NotificationCompat.MediaStyle mediaStyle = new NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0)
                    .setMediaSession(mMediaSession.getSessionToken());
            builder.setStyle(mediaStyle);

            // TODO change small icon (notification)
            builder.setSmallIcon(R.mipmap.ic_launcher);

            if (state == PlaybackStateCompat.STATE_PAUSED) {
                builder.addAction(new NotificationCompat.Action(
                        android.R.drawable.ic_media_play, getString(R.string.play),
                        MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE))
                );
            } else {
                builder.addAction(new NotificationCompat.Action(
                        android.R.drawable.ic_media_pause,
                        getString(R.string.pause),
                        MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE))
                );
            }

            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void setMediaPlaybackState(int state) {
        PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder();
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PAUSE);
        } else {
            playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PLAY);
        }
        playbackStateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0);
        mMediaSession.setPlaybackState(playbackStateBuilder.build());
    }

    private void initMediaSessionMetadata() {

        // TODO everything
        MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();

        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "Beo Dat May Troi");
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, "Singer: Anh Tho");

        mMediaSession.setMetadata(metadataBuilder.build());
    }

    private boolean hasAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        return result == AudioManager.AUDIOFOCUS_GAIN;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        unregisterReceiver(mHeadphonesReceiver);
        mMediaSession.release();
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
    }
}

package com.icarapovic.metronome.ui.activities;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.adapters.PagerAdapter;
import com.icarapovic.metronome.service.MediaController;
import com.icarapovic.metronome.service.MediaService;
import com.icarapovic.metronome.ui.fragments.AlbumFragment;
import com.icarapovic.metronome.ui.fragments.ArtistFragment;
import com.icarapovic.metronome.ui.fragments.GenresFragment;
import com.icarapovic.metronome.ui.fragments.PlaylistFragment;
import com.icarapovic.metronome.ui.fragments.SongFragment;
import com.icarapovic.metronome.utils.MediaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LibraryActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_STORAGE = 100;
    private static final int REQUEST_CODE_APP_SETTINGS = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigation;
    @BindView(R.id.tab_bar)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.mini_player)
    LinearLayout miniPlayer;
    @BindView(R.id.song_name)
    TextView songName;
    @BindView(R.id.play_pause)
    ImageView playPause;

    private ActionBarDrawerToggle toggle;
    private BroadcastReceiver syncListener;
    private MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // activate BindView annotations
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        syncListener = createSyncListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            initViewPagerWithTabs();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(syncListener, new IntentFilter(MediaService.ACTION_SYNC_STATE));
        if (controller != null) {
            updateMiniPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                initViewPagerWithTabs();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(syncListener);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        int storageAccess = checkSelfPermission(WRITE_EXTERNAL_STORAGE);

        if (storageAccess == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_STORAGE);
        } else {
            initViewPagerWithTabs();
        }
    }

    private void initNavigationDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.label_open_drawer, R.string.label_close_drawer);
        drawerLayout.addDrawerListener(toggle);

        // sync toggle with navigation drawer, showing arrow or hamburger menu
        toggle.syncState();
    }

    private void initViewPagerWithTabs() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SongFragment.newInstance(), SongFragment.getTitle());
        adapter.addFragment(AlbumFragment.newInstance(), AlbumFragment.getTitle());
        adapter.addFragment(ArtistFragment.newInstance(), ArtistFragment.getTitle());
        adapter.addFragment(GenresFragment.newInstance(), GenresFragment.getTitle());
        adapter.addFragment(PlaylistFragment.newInstance(), PlaylistFragment.getTitle());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initViewPagerWithTabs();
            } else if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_STORAGE);
                            }
                        }).setMessage(R.string.permission_rationale_storage)
                        .setCancelable(false)
                        .setTitle(R.string.permission_rationale_dialog_title)
                        .create()
                        .show();
            } else {
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.setData(Uri.parse("package:" + getPackageName()));

                new AlertDialog.Builder(this)
                        .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivityForResult(i, REQUEST_CODE_APP_SETTINGS);
                            }
                        })
                        .setNegativeButton(R.string.label_exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finishAndRemoveTask();
                            }
                        })
                        .setMessage(R.string.permission_manual_settings)
                        .setCancelable(false)
                        .setTitle(R.string.permission_rationale_dialog_title)
                        .create()
                        .show();
            }
        }
    }

    private BroadcastReceiver createSyncListener() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                if (i.getAction().equals(MediaService.ACTION_SYNC_STATE)) {
                    if (controller == null) {
                        controller = MediaUtils.getMediaController();
                    }

                    updateMiniPlayer();
                }
            }
        };
    }

    private void updateMiniPlayer() {
        miniPlayer.setVisibility(controller.getActiveSong() != null ? VISIBLE : GONE);
        songName.setText(controller.getActiveSong().getTitle());
        playPause.setImageResource(controller.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @OnClick(R.id.play_pause)
    public void onPlayPause() {
        if (controller.isPlaying()) {
            controller.pause();
        } else {
            controller.play();
        }
        updateMiniPlayer();
    }

    @OnClick(R.id.mini_player)
    public void openNowPlaying() {
        startActivity(new Intent(this, NowPlayingActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == REQUEST_CODE_APP_SETTINGS) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                initViewPagerWithTabs();
            } else {
                finishAndRemoveTask();
            }
        }
    }
}


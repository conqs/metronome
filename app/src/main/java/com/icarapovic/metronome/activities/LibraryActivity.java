package com.icarapovic.metronome.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.adapters.SongAdapter;
import com.icarapovic.metronome.provider.LocalMediaProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class LibraryActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE = 100;

    @BindView(R.id.recycler_song)
    RecyclerView songRecycler;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigation;

    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // activate BindView annotations
        ButterKnife.bind(this);

        init();

        // if permission missing, ask for it
        if(!EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            EasyPermissions.requestPermissions(this, getString(R.string.info_permissions_external_storage),
                    REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            onContinue();
        }
    }

    private void init() {
        setSupportActionBar(mToolbar);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mToggle);

        // sync toggle with navigation drawer, showing arrow or hamburger menu
        mToggle.syncState();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // auto handle permission result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        onContinue();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // without permission to read storage this app is useless (for now)
        finishAndRemoveTask();
    }

    // continue after permission check, method is NOT from activity lifecycle
    private void onContinue(){
        // create adapter and set it to the recycler view
        SongAdapter adapter = new SongAdapter(LocalMediaProvider.getInstance().fetchSongs(this));
        songRecycler.setLayoutManager(new LinearLayoutManager(this));
        songRecycler.setAdapter(adapter);
    }
}

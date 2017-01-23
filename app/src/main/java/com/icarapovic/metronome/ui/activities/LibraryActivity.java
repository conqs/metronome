package com.icarapovic.metronome.ui.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.ui.adapters.PagerAdapter;
import com.icarapovic.metronome.ui.fragments.AlbumFragment;
import com.icarapovic.metronome.ui.fragments.ArtistFragment;
import com.icarapovic.metronome.ui.fragments.GenresFragment;
import com.icarapovic.metronome.ui.fragments.PlaylistFragment;
import com.icarapovic.metronome.ui.fragments.SongFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class LibraryActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE = 100;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigation;
    @BindView(R.id.tab_bar)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // activate BindView annotations
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        initNavigationDrawer();
        initViewPager();
        initTabBar();

        // if permission missing, ask for it
        if(!EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            EasyPermissions.requestPermissions(this, getString(R.string.info_permissions_external_storage),
                    REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            onContinue();
        }
    }

    private void initTabBar() {
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initNavigationDrawer() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mToggle);

        // sync toggle with navigation drawer, showing arrow or hamburger menu
        mToggle.syncState();
    }

    private void initViewPager() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SongFragment.newInstance(), SongFragment.getTitle());
        adapter.addFragment(AlbumFragment.newInstance(), AlbumFragment.getTitle());
        adapter.addFragment(ArtistFragment.newInstance(), ArtistFragment.getTitle());
        adapter.addFragment(GenresFragment.newInstance(), GenresFragment.getTitle());
        adapter.addFragment(PlaylistFragment.newInstance(), PlaylistFragment.getTitle());
        mViewPager.setAdapter(adapter);
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

    }
}

package com.icarapovic.metronome.ui.activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.adapters.PagerAdapter;
import com.icarapovic.metronome.ui.fragments.AlbumFragment;
import com.icarapovic.metronome.ui.fragments.ArtistFragment;
import com.icarapovic.metronome.ui.fragments.GenresFragment;
import com.icarapovic.metronome.ui.fragments.PlaylistFragment;
import com.icarapovic.metronome.ui.fragments.SongFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LibraryActivity extends BaseActivity {

    private static final int REQUEST_CODE_PERMISSION_STORAGE = 100;
    private static final int REQUEST_CODE_APP_SETTINGS = 1;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            initViewPagerWithTabs();
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
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.label_open_drawer, R.string.label_close_drawer);
        mDrawerLayout.addDrawerListener(mToggle);

        // sync toggle with navigation drawer, showing arrow or hamburger menu
        mToggle.syncState();
    }

    private void initViewPagerWithTabs() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SongFragment.newInstance(), SongFragment.getTitle());
        adapter.addFragment(AlbumFragment.newInstance(), AlbumFragment.getTitle());
        adapter.addFragment(ArtistFragment.newInstance(), ArtistFragment.getTitle());
        adapter.addFragment(GenresFragment.newInstance(), GenresFragment.getTitle());
        adapter.addFragment(PlaylistFragment.newInstance(), PlaylistFragment.getTitle());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
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


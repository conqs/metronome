package com.icarapovic.metronome.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icarapovic.metronome.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumFragment extends Fragment {
    public static final String TAG = "com.icarapovic.metronome.ALBUM_FRAGMENT";

    @BindView(R.id.recycler_album)
    RecyclerView albumRecycler;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    public static String getTitle() {
        // TODO extract
        return "Album";
    }
}
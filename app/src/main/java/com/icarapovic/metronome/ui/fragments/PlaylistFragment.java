package com.icarapovic.metronome.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.adapters.PlaylistAdapter;
import com.icarapovic.metronome.provider.local.LocalMediaProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistFragment extends Fragment {
    public static final String TAG = "com.icarapovic.metronome.PLAYLIST_FRAGMENT";

    @BindView(R.id.recycler_playlist)
    RecyclerView mPlaylistRecycler;

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    public static String getTitle() {
        // TODO extract
        return "Playlists";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        PlaylistAdapter adapter = new PlaylistAdapter(LocalMediaProvider.getInstance().fetchPlaylists(getContext()));
        mPlaylistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlaylistRecycler.setAdapter(adapter);
    }
}

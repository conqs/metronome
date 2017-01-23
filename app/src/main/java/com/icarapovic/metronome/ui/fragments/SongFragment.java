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
import com.icarapovic.metronome.provider.LocalMediaProvider;
import com.icarapovic.metronome.ui.adapters.SongAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongFragment extends Fragment {
    public static final String TAG = "com.icarapovic.metronome.SONG_FRAGMENT";

    @BindView(R.id.recycler_song)
    RecyclerView songRecycler;

    public static SongFragment newInstance() {
        return new SongFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_songs, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // create adapter and set it to the recycler view
        SongAdapter adapter = new SongAdapter(LocalMediaProvider.getInstance().fetchSongs(getContext()));
        songRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        songRecycler.setAdapter(adapter);
    }

    public static String getTitle() {
        // TODO extract
        return "Songs";
    }
}

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
import com.icarapovic.metronome.adapters.ArtistAdapter;
import com.icarapovic.metronome.provider.local.LocalMediaProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistFragment extends Fragment {
    public static final String TAG = "com.icarapovic.metronome.ARTIST_FRAGMENT";

    @BindView(R.id.recycler_artist)
    RecyclerView mArtistRecycler;

    public static ArtistFragment newInstance() {
        return new ArtistFragment();
    }

    public static String getTitle() {
        // TODO extract
        return "Artist";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ArtistAdapter adapter = new ArtistAdapter(LocalMediaProvider.getInstance().fetchArtists(getContext()));
        mArtistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mArtistRecycler.setAdapter(adapter);
    }
}

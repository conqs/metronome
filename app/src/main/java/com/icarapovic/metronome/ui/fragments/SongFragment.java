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
import com.icarapovic.metronome.adapters.SongAdapter;
import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.provider.local.LocalMediaProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SongFragment extends Fragment implements Observer<List<Song>> {
    public static final String TAG = "com.icarapovic.metronome.SONG_FRAGMENT";
    private static final String FRAGMENT_TITLE = "Songs";

    @BindView(R.id.recycler_song)
    RecyclerView songRecycler;

    public static SongFragment newInstance() {
        return new SongFragment();
    }

    public static String getTitle() {
        return FRAGMENT_TITLE;
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
        LocalMediaProvider.getInstance().fetchSongs(getContext(), this);

    }

    @Override
    public void onSubscribe(Disposable d) {
        // Not needed, there is no reason to cancel the request
    }

    @Override
    public void onNext(List<Song> songs) {
        // create adapter and set it to the recycler view
        SongAdapter adapter = new SongAdapter(songs);
        songRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        songRecycler.setAdapter(adapter);
    }

    @Override
    public void onError(Throwable e) {
        // TODO
    }

    @Override
    public void onComplete() {
        // Nothing to do here...
    }
}

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
import com.icarapovic.metronome.models.Artist;
import com.icarapovic.metronome.provider.local.LocalMediaProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ArtistFragment extends Fragment implements Observer<List<Artist>> {
    public static final String TAG = "com.icarapovic.metronome.ARTIST_FRAGMENT";
    private static final String FRAGMENT_TITLE = "Artist";

    @BindView(R.id.recycler_artist)
    RecyclerView mArtistRecycler;

    public static ArtistFragment newInstance() {
        return new ArtistFragment();
    }

    public static String getTitle() {
        return FRAGMENT_TITLE;
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
        LocalMediaProvider.getInstance().fetchArtists(getContext(), this);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(List<Artist> artists) {
        ArtistAdapter adapter = new ArtistAdapter(artists);
        mArtistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mArtistRecycler.setAdapter(adapter);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}

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
import com.icarapovic.metronome.adapters.GenreAdapter;
import com.icarapovic.metronome.models.Genre;
import com.icarapovic.metronome.provider.local.LocalMediaProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GenresFragment extends Fragment implements Observer<List<Genre>> {
    public static final String TAG = "com.icarapovic.metronome.GENRE_FRAGMENT";

    @BindView(R.id.recycler_genres)
    RecyclerView mGenresRecycler;

    public static GenresFragment newInstance() {
        return new GenresFragment();
    }

    public static String getTitle() {
        // TODO extract
        return "Genres";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_genres, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LocalMediaProvider.getInstance().fetchGenres(getContext(), this);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(List<Genre> genres) {
        GenreAdapter adapter = new GenreAdapter(genres);
        mGenresRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mGenresRecycler.setAdapter(adapter);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}

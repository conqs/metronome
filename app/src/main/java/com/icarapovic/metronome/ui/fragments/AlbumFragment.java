package com.icarapovic.metronome.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.adapters.AlbumAdapter;
import com.icarapovic.metronome.models.Album;
import com.icarapovic.metronome.provider.local.LocalMediaProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AlbumFragment extends Fragment implements Observer<List<Album>> {
    public static final String TAG = "com.icarapovic.metronome.ALBUM_FRAGMENT";

    @BindView(R.id.recycler_album)
    RecyclerView mAlbumRecycler;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    public static String getTitle() {
        // TODO extract
        return "Album";
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
        LocalMediaProvider.getInstance().fetchAlbums(getContext(), this);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(List<Album> albums) {
        AlbumAdapter adapter = new AlbumAdapter(albums);
        mAlbumRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAlbumRecycler.setAdapter(adapter);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}

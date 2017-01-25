package com.icarapovic.metronome.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Album;
import com.icarapovic.metronome.utils.MediaUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> mAlbums;

    public AlbumAdapter(List<Album> albums) {
        mAlbums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate layout into a View and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // fill ViewHolder with data
        MediaUtils.loadAlbumArt(mAlbums.get(position).getAlbumId(), holder.albumArt);
        // TODO: Make album art ImageView square!
    }

    @Override
    public int getItemCount() {
        // size of the data set
        return mAlbums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.album_art)
        ImageView albumArt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

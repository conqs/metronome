package com.icarapovic.metronome.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.utils.MediaUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private List<Song> mSongs;

    public SongAdapter(List<Song> songs){
        mSongs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate layout into a View and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // fill ViewHolder with data
        MediaUtils.loadAlbumArt(mSongs.get(position).getAlbumId(), holder.albumArt);
        holder.songTitle.setText(mSongs.get(position).getTitle());
        holder.songArtist.setText(mSongs.get(position).getArtistName());
    }

    @Override
    public int getItemCount() {
        // size of the data set
        return mSongs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.album_art)
        ImageView albumArt;
        @BindView(R.id.song_title)
        TextView songTitle;
        @BindView(R.id.song_artist)
        TextView songArtist;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

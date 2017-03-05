package com.icarapovic.metronome.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.ui.activities.NowPlayingActivity;
import com.icarapovic.metronome.utils.MediaUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private List<Song> songs;

    public SongAdapter(List<Song> songs) {
        this.songs = songs;
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
        float artworkQuality = 1.0f;
        MediaUtils.loadSongArt(songs.get(position).getId(), holder.albumArt, artworkQuality);
        holder.songTitle.setText(songs.get(position).getTitle());
        holder.songArtist.setText(songs.get(position).getArtistName());
        holder.song = songs.get(position);
    }

    @Override
    public int getItemCount() {
        // size of the data set
        return songs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.album_art)
        ImageView albumArt;
        @BindView(R.id.song_title)
        TextView songTitle;
        @BindView(R.id.song_artist)
        TextView songArtist;

        Song song;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @OnClick(R.id.layout)
        public void play() {
            MediaUtils.getMediaController().play();
            context.startActivity(new Intent(context, NowPlayingActivity.class));
        }
    }
}

package com.icarapovic.metronome.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Playlist;
import com.icarapovic.metronome.provider.LocalMediaProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<Playlist> mPlaylists;

    public PlaylistAdapter(List<Playlist> playlists) {
        mPlaylists = playlists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate layout into a View and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.playlistName.getContext();

        Playlist playlist = mPlaylists.get(position);
        holder.playlistName.setText(playlist.getName());

        int playlistSongCount = LocalMediaProvider.getInstance()
                .fetchSongsFromPlaylist(context, playlist.getId())
                .size();
        holder.PlaylistSongCount.setText(context.getString(R.string.label_song_count, playlistSongCount));
    }

    @Override
    public int getItemCount() {
        // size of the data set
        return mPlaylists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.playlist_name)
        TextView playlistName;
        @BindView(R.id.playlist_song_count)
        TextView PlaylistSongCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

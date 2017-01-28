package com.icarapovic.metronome.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Artist;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private List<Artist> mArtists;

    public ArtistAdapter(List<Artist> artists) {
        mArtists = artists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate layout into a View and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.artistMetadata.getContext();

        Artist artist = mArtists.get(position);

        holder.artistName.setText(artist.getArtistName());
        holder.artistMetadata.setText(context.getString(
                R.string.label_album_song_count, artist.getNumberOfAlbums(), artist.getNumberOfSongs()));
    }

    @Override
    public int getItemCount() {
        // size of the data set
        return mArtists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.artist_metadata)
        TextView artistMetadata;
        @BindView(R.id.artist_name)
        TextView artistName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

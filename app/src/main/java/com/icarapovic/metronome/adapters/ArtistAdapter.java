package com.icarapovic.metronome.adapters;

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
    private StringBuilder sb = new StringBuilder();

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
        Artist artist = mArtists.get(position);
        sb.replace(0, sb.length(), "");

        // format "X albums  |  X songs"
        sb.append(artist.getNumberOfAlbums())
                .append(" albums  |  ")
                .append(artist.getNumberOfSongs())
                .append(" songs");

        holder.artistName.setText(artist.getArtistName());
        holder.artistMetadata.setText(sb.toString());
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

package com.icarapovic.metronome.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icarapovic.metronome.R;
import com.icarapovic.metronome.models.Genre;
import com.icarapovic.metronome.provider.LocalMediaProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private List<Genre> mGenres;
    private StringBuilder sb = new StringBuilder();

    public GenreAdapter(List<Genre> Genres) {
        mGenres = Genres;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate layout into a View and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = mGenres.get(position);
        holder.genreName.setText(genre.getGenreName());

        int genreSongCount = LocalMediaProvider.getInstance()
                .fetchSongsFromGenre(holder.genreName.getContext(), genre.getGenreId())
                .size();
        holder.genreSongCount.setText(genreSongCount + " songs");
    }

    @Override
    public int getItemCount() {
        // size of the data set
        return mGenres.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.genre_name)
        TextView genreName;
        @BindView(R.id.genre_song_count)
        TextView genreSongCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

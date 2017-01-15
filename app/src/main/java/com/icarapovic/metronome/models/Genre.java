package com.icarapovic.metronome.models;

class Genre {

    public Genre(int genreId, String genreName) {
        mGenreId = genreId;
        mGenreName = genreName;
    }

    private final int mGenreId;
    private final String mGenreName;

    public int getGenreId() {
        return mGenreId;
    }

    public String getGenreName() {
        return mGenreName;
    }

    @Override
    public String toString() {
        return "Genre [ id=" + mGenreId + ", name=" + mGenreName + " ]";
    }
}

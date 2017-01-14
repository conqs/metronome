package com.icarapovic.metronome.models;

import com.icarapovic.metronome.utils.MediaUtils;

class Song {

    private Song(int id, int albumId, Album album, int artistId, Artist artist, long duration, int genreId, Genre genre) {
        mId = id;
        mAlbumId = albumId;
        mAlbum = album;
        mArtistId = artistId;
        mArtist = artist;
        mDuration = duration;
        mGenreId = genreId;
        mGenre = genre;
    }

    private final int mId;
    private final int mAlbumId;
    private final Album mAlbum;
    private final int mArtistId;
    private final Artist mArtist;
    private final long mDuration;
    private final int mGenreId;
    private final Genre mGenre;

    private static class Builder {
        private int mId;
        private int mAlbumId;
        private Album mAlbum;
        private int mArtistId;
        private Artist mArtist;
        private long mDuration;
        private int mGenreId;
        private Genre mGenre;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setAlbumId(int albumId) {
            mAlbumId = albumId;
            return this;
        }

        public Builder setArtistId(int artistId) {
            mArtistId = artistId;
            return this;
        }

        public Builder setDuration(long duration) {
            mDuration = duration;
            return this;
        }

        public Builder setGenreId(int genreId) {
            mGenreId = genreId;
            return this;
        }

        public Builder setAlbum(Album album) {
            mAlbum = album;
            return this;
        }

        public Builder setArtist(Artist artist) {
            mArtist = artist;
            return this;
        }

        public Builder setGenre(Genre genre) {
            mGenre = genre;
            return this;
        }

        public Song build() {
            return new Song(mId, mAlbumId, mAlbum, mArtistId, mArtist, mDuration, mGenreId, mGenre);
        }

    }

    public int getId() {
        return mId;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public int getArtistId() {
        return mArtistId;
    }

    public long getDuration() {
        return mDuration;
    }

    public int getGenreId() {
        return mGenreId;
    }

    public Album getAlbum() {
        return mAlbum;
    }

    public Artist getArtist() {
        return mArtist;
    }

    public Genre getGenre() {
        return mGenre;
    }

    @Override
    public String toString() {
        return "Song [ id=" + mId +
                ", albumId=" + mAlbumId +
                ", artistId=" + mArtistId +
                ", duration=" + MediaUtils.formatDuration(mDuration) +
                ", genreId=" + mGenreId +
                " ]";
    }
}

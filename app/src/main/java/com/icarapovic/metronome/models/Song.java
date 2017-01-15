package com.icarapovic.metronome.models;

import com.icarapovic.metronome.utils.MediaUtils;

public class Song {

    private Song(
            int id,
            int albumId,
            Album album,
            int artistId,
            Artist artist,
            long duration,
            int genreId,
            Genre genre,
            String path) {

        mId = id;
        mAlbumId = albumId;
        mAlbum = album;
        mArtistId = artistId;
        mArtist = artist;
        mDuration = duration;
        mGenreId = genreId;
        mGenre = genre;
        mPath = path;
    }

    private final int mId;
    private final int mAlbumId;
    private final Album mAlbum;
    private final int mArtistId;
    private final Artist mArtist;
    private final long mDuration;
    private final int mGenreId;
    private final Genre mGenre;
    private final String mPath;

    private static class Builder {
        private int mId;
        private int mAlbumId;
        private Album mAlbum;
        private int mArtistId;
        private Artist mArtist;
        private long mDuration;
        private int mGenreId;
        private Genre mGenre;
        private String mPath;

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

        public Builder setPath(String path) {
            mPath = path;
            return this;
        }

        public Song build() {
            return new Song(mId, mAlbumId, mAlbum, mArtistId, mArtist, mDuration, mGenreId, mGenre, mPath);
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

    public String getPath() {
        return mPath;
    }

    @Override
    public String toString() {
        return "Song [ id=" + mId +
                ", albumId=" + mAlbumId +
                ", artistId=" + mArtistId +
                ", duration=" + MediaUtils.formatDuration(mDuration) +
                ", genreId=" + mGenreId +
                ", path=" + mPath +
                " ]";
    }
}

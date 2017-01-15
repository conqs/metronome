package com.icarapovic.metronome.models;

public class Album {

    private Album(int albumId, String albumTitle, Artist artist, int artistId, int numberOfSongs) {
        mAlbumId = albumId;
        mAlbumTitle = albumTitle;
        mArtist = artist;
        mArtistId = artistId;
        mNumberOfSongs = numberOfSongs;
    }

    private final int mAlbumId;
    private final String mAlbumTitle;
    private final Artist mArtist;
    private final int mArtistId;
    private final int mNumberOfSongs;

    private static class Builder {
        private int mAlbumId;
        private String mAlbumTitle;
        private Artist mArtist;
        private int mArtistId;
        private int mNumberOfSongs;

        public Builder setAlbumId(int id){
            mAlbumId = id;
            return this;
        }

        public Builder setAlbumTitle(String title){
            mAlbumTitle = title;
            return this;
        }

        public Builder setArtist(Artist artist){
            mArtist = artist;
            return this;
        }

        public Builder setArtistiId(int artistId){
            mArtistId = artistId;
            return this;
        }

        public Builder setNumberOfSongs(int numberOfSongs){
            mNumberOfSongs = numberOfSongs;
            return this;
        }

        public Album build(){
            return new Album(mAlbumId, mAlbumTitle, mArtist, mArtistId, mNumberOfSongs);
        }
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public String getAlbumTitle() {
        return mAlbumTitle;
    }

    public Artist getArtist() {
        return mArtist;
    }

    public int getArtistId() {
        return mArtistId;
    }

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    @Override
    public String toString() {
        return "Album [ id=" + mAlbumId
                + ", title=" + mAlbumTitle
                + ", artist=" + mArtist.getArtistName()
                + ", numOfSongs=" + mNumberOfSongs + " ]";
    }
}

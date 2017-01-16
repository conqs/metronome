package com.icarapovic.metronome.models;

public class Artist {

    private Artist(int id, String artistName, int numberOfAlbums, int numberOfSongs) {
        mId = id;
        mArtistName = artistName;
        mNumberOfAlbums = numberOfAlbums;
        mNumberOfSongs = numberOfSongs;
    }

    private final int mId;
    private final String mArtistName;
    private final int mNumberOfAlbums;
    private final int mNumberOfSongs;

    public int getId() {
        return mId;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public int getNumberOfAlbums() {
        return mNumberOfAlbums;
    }

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    public static class Builder {

        private int mId;
        private String mArtistName;
        private int mNumberOfAlbums;
        private int mNumberOfSongs;

        public Builder setId(int id){
            mId = id;
            return this;
        }

        public Builder setArtistName(String artistName){
            mArtistName = artistName;
            return this;
        }

        public Builder setNumberOfAlbums(int numberOfAlbums){
            mNumberOfAlbums = numberOfAlbums;
            return this;
        }

        public Builder setNumberOfSongs(int numberOfSongs){
            mNumberOfSongs = numberOfSongs;
            return this;
        }

        public Artist build(){
            return new Artist(mId, mArtistName, mNumberOfAlbums, mNumberOfSongs);
        }
    }

    @Override
    public String toString() {
        return "Artist [ id=" + mId
                + ", name=" + mArtistName
                + ", numOfAlbums=" + mNumberOfAlbums
                + ", numOfSongs=" + mNumberOfSongs + " ]";
    }
}

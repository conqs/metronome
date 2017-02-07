package com.icarapovic.metronome.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    private Album(int albumId, String albumTitle, String artist, int numberOfSongs, String albumArt) {
        mAlbumId = albumId;
        mAlbumTitle = albumTitle;
        mArtist = artist;
        mNumberOfSongs = numberOfSongs;
        mAlbumArt = albumArt;
    }

    private final int mAlbumId;
    private final String mAlbumTitle;
    private final String mArtist;
    private final int mNumberOfSongs;
    private final String mAlbumArt;

    protected Album(Parcel in) {
        mAlbumId = in.readInt();
        mAlbumTitle = in.readString();
        mArtist = in.readString();
        mNumberOfSongs = in.readInt();
        mAlbumArt = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mAlbumId);
        dest.writeString(mAlbumTitle);
        dest.writeString(mArtist);
        dest.writeInt(mNumberOfSongs);
        dest.writeString(mAlbumArt);
    }

    public static class Builder {
        private int mAlbumId;
        private String mAlbumTitle;
        private String mArtist;
        private int mNumberOfSongs;
        private String mAlbumArt;

        public Builder setAlbumId(int id){
            mAlbumId = id;
            return this;
        }

        public Builder setAlbumTitle(String title){
            mAlbumTitle = title;
            return this;
        }

        public Builder setArtist(String artist){
            mArtist = artist;
            return this;
        }

        public Builder setNumberOfSongs(int numberOfSongs){
            mNumberOfSongs = numberOfSongs;
            return this;
        }

        public Builder setAlbumArt(String albumArt){
            mAlbumArt = albumArt;
            return this;
        }

        public Album build(){
            return new Album(mAlbumId, mAlbumTitle, mArtist, mNumberOfSongs, mAlbumArt);
        }
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public String getAlbumTitle() {
        return mAlbumTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    public String getAlbumArt() {
        return mAlbumArt;
    }

    @Override
    public String toString() {
        return "Album [ id=" + mAlbumId
                + ", title=" + mAlbumTitle
                + ", artist=" + mArtist
                + ", numOfSongs=" + mNumberOfSongs + " ]";
    }
}

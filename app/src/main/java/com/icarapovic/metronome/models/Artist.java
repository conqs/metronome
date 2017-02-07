package com.icarapovic.metronome.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {

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

    protected Artist(Parcel in) {
        mId = in.readInt();
        mArtistName = in.readString();
        mNumberOfAlbums = in.readInt();
        mNumberOfSongs = in.readInt();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mArtistName);
        dest.writeInt(mNumberOfAlbums);
        dest.writeInt(mNumberOfSongs);
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

package com.icarapovic.metronome.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {

    public Genre(int genreId, String genreName) {
        mGenreId = genreId;
        mGenreName = genreName;
    }

    private final int mGenreId;
    private final String mGenreName;

    protected Genre(Parcel in) {
        mGenreId = in.readInt();
        mGenreName = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mGenreId);
        dest.writeString(mGenreName);
    }
}

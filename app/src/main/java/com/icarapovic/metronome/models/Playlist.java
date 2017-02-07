package com.icarapovic.metronome.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Playlist implements Parcelable {

    private final int mId;
    private final String mName;
    private final String mUri;
    private final long mDateAdded;
    private final long mDateModified;

    private Playlist(int id, String name, String uri, long created, long modified) {
        this.mId = id;
        this.mName = name;
        this.mUri = uri;
        this.mDateAdded = created;
        this.mDateModified = modified;
    }

    protected Playlist(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mUri = in.readString();
        mDateAdded = in.readLong();
        mDateModified = in.readLong();
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUri() {
        return mUri;
    }

    public long getDateAdded() {
        return mDateAdded;
    }

    public long getDateModified() {
        return mDateModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mUri);
        dest.writeLong(mDateAdded);
        dest.writeLong(mDateModified);
    }

    public static class Builder {
        private int id;
        private String name;
        private String uri;
        private long dateAdded;
        private long dateModified;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setDateAdded(long dateAdded) {
            this.dateAdded = dateAdded;
            return this;
        }

        public Builder setDateModified(long dateModified) {
            this.dateModified = dateModified;
            return this;
        }

        public Playlist build() {
            return new Playlist(id, name, uri, dateAdded, dateModified);
        }
    }
}

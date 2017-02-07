package com.icarapovic.metronome.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.icarapovic.metronome.utils.MediaUtils;

public class Song implements Parcelable {

    private Song(
            int id,
            String title,
            int albumId,
            String album,
            int artistId,
            String artist,
            long duration,
            String path,
            long size,
            long dateAdded) {

        mId = id;
        mAlbumId = albumId;
        mAlbum = album;
        mArtistId = artistId;
        mArtist = artist;
        mDuration = duration;
        mPath = path;
        mFileSize = size;
        mDateAdded = dateAdded;
        mTitle = title;
    }

    private final int mId;
    private final String mTitle;
    private final int mAlbumId;
    private final String mAlbum;
    private final int mArtistId;
    private final String mArtist;
    private final long mDuration;
    private final String mPath;
    private final long mFileSize;
    private final long mDateAdded;

    @Override
    public int describeContents() {
        return 0;
    }

    private Song(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mAlbumId = in.readInt();
        mAlbum = in.readString();
        mArtistId = in.readInt();
        mArtist = in.readString();
        mDuration = in.readLong();
        mPath = in.readString();
        mFileSize = in.readLong();
        mDateAdded = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeInt(mAlbumId);
        dest.writeString(mAlbum);
        dest.writeInt(mArtistId);
        dest.writeString(mArtist);
        dest.writeLong(mDuration);
        dest.writeString(mPath);
        dest.writeLong(mFileSize);
        dest.writeLong(mDateAdded);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public static class Builder {
        private int mId;
        private String mTitle;
        private int mAlbumId;
        private String mAlbum;
        private int mArtistId;
        private String mArtist;
        private long mDuration;
        private String mPath;
        private long mFileSize;
        private long mDateAdded;

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

        public Builder setAlbum(String album) {
            mAlbum = album;
            return this;
        }

        public Builder setArtist(String artist) {
            mArtist = artist;
            return this;
        }

        public Builder setDateAdded(long dateAdded) {
            mDateAdded = dateAdded;
            return this;
        }

        public Builder setPath(String path) {
            mPath = path;
            return this;
        }

        public Builder setFileSize(long fileSize){
            mFileSize = fileSize;
            return this;
        }

        public Builder setTitle(String title){
            mTitle = title;
            return this;
        }

        public Song build() {
            return new Song(mId, mTitle, mAlbumId, mAlbum, mArtistId, mArtist, mDuration, mPath, mFileSize, mDateAdded);
        }

    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
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

    public String getAlbumName() {
        return mAlbum;
    }

    public String getArtistName() {
        return mArtist;
    }

    public long getDateAdded() {
        return mDateAdded;
    }

    public String getPath() {
        return mPath;
    }

    public long getFileSize() {
        return mFileSize;
    }

    @Override
    public String toString() {
        return "Song [ id=" + mId +
                ", title=" + mTitle +
                ", album=" + mAlbum +
                ", artist=" + mArtist +
                ", duration=" + MediaUtils.formatDuration(mDuration) +
                ", dateAdded=" + mDateAdded +
                ", path=" + mPath +
                ", fileSize=" + mFileSize +
                " ]";
    }
}

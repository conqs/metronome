package com.icarapovic.metronome.models;

public class Playlist {

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

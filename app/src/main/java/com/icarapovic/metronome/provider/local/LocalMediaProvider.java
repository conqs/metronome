package com.icarapovic.metronome.provider.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.icarapovic.metronome.models.Album;
import com.icarapovic.metronome.models.Artist;
import com.icarapovic.metronome.models.Genre;
import com.icarapovic.metronome.models.Playlist;
import com.icarapovic.metronome.models.Song;
import com.icarapovic.metronome.provider.MediaProvider;

import java.util.ArrayList;
import java.util.List;

public class LocalMediaProvider implements MediaProvider {

    private static LocalMediaProvider INSTANCE;

    private List<Song> cachedSongList;
    private List<Album> cachedAlbumList;
    private List<Artist> cachedArtistList;
    private List<Genre> cachedGenreList;
    private List<Playlist> cachedPlaylists;

    private LocalMediaProvider() {
    }

    public static LocalMediaProvider getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        INSTANCE = new LocalMediaProvider();
        return INSTANCE;
    }

    @Override
    public List<Song> fetchSongs(Context context) {
        // if cached, return cache immediately
        if (cachedSongList != null) {
            return cachedSongList;
        } else {
            cachedSongList = new ArrayList<>();
        }

        Cursor cursor = getSongCursor(context);

        if (cursor != null) {
            Song song;

            // build song object for each db entry
            while (cursor.moveToNext()) {
                song = new Song.Builder()
                        .setId(cursor.getInt(0))
                        .setTitle(cursor.getString(1))
                        .setAlbumId(cursor.getInt(2))
                        .setAlbum(cursor.getString(3))
                        .setArtistId(cursor.getInt(4))
                        .setArtist(cursor.getString(5))
                        .setDuration(cursor.getLong(6))
                        .setPath(cursor.getString(7))
                        .setDateAdded(cursor.getLong(8))
                        .setFileSize(cursor.getLong(9))
                        .build();

                // fill cache
                cachedSongList.add(song);
            }
            // close data set
            cursor.close();
        }

        return cachedSongList;
    }

    @Override
    public List<Artist> fetchArtists(Context context) {

        if (cachedArtistList != null) {
            return cachedArtistList;
        } else {
            cachedArtistList = new ArrayList<>();
        }

        Cursor cursor = getArtistCursor(context);

        if (cursor != null) {
            Artist artist;
            while (cursor.moveToNext()) {
                artist = new Artist.Builder()
                        .setId(cursor.getInt(0))
                        .setArtistName(cursor.getString(1))
                        .setNumberOfAlbums(cursor.getInt(2))
                        .setNumberOfSongs(cursor.getInt(3))
                        .build();
                cachedArtistList.add(artist);
            }

            cursor.close();
        }


        return cachedArtistList;
    }

    @Override
    public List<Album> fetchAlbums(Context context) {
        if (cachedAlbumList != null) {
            return cachedAlbumList;
        } else {
            cachedAlbumList = new ArrayList<>();
        }

        Cursor c = getAlbumCursor(context);

        if (c != null) {
            Album album;
            while (c.moveToNext()) {
                album = new Album.Builder()
                        .setAlbumId(c.getInt(0))
                        .setAlbumTitle(c.getString(1))
                        .setAlbumArt(c.getString(2))
                        .setArtist(c.getString(3))
                        .setNumberOfSongs(c.getInt(4))
                        .build();
                cachedAlbumList.add(album);
            }

            c.close();
        }

        return cachedAlbumList;
    }

    @Override
    public List<Genre> fetchGenres(Context context) {
        if (cachedGenreList != null) {
            return cachedGenreList;
        } else {
            cachedGenreList = new ArrayList<>();
        }

        Cursor c = getGenreCursor(context);

        if (c != null) {
            while (c.moveToNext()) {
                cachedGenreList.add(new Genre(c.getInt(0), c.getString(1)));
            }
            c.close();
        }

        return cachedGenreList;
    }

    @Override
    public List<Playlist> fetchPlaylists(Context context) {
        if (cachedPlaylists != null) {
            return cachedPlaylists;
        } else {
            cachedPlaylists = new ArrayList<>();
        }

        Cursor c = getPlaylistCursor(context);

        if (c != null) {
            Playlist playlist;
            while (c.moveToNext()) {
                playlist = new Playlist.Builder()
                        .setId(c.getInt(0))
                        .setName(c.getString(1))
                        .setUri(c.getString(2))
                        .setDateAdded(c.getLong(3))
                        .setDateModified(c.getLong(4))
                        .build();

                cachedPlaylists.add(playlist);
            }
            c.close();
        }

        return cachedPlaylists;
    }

    @Override
    public List<Song> fetchSongsFromAlbum(Context context, int albumId) {
        List<Song> songs = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Projections.SONG,
                MediaStore.Audio.Media.ALBUM_ID + " = ?", // WHERE albumId = ?
                new String[albumId],  // fills the ? in previous line
                MediaStore.Audio.AudioColumns.TITLE + " ASC"
        );

        if (cursor != null) {
            Song song;
            while (cursor.moveToNext()) {
                song = new Song.Builder()
                        .setId(cursor.getInt(0))
                        .setTitle(cursor.getString(1))
                        .setAlbumId(cursor.getInt(2))
                        .setAlbum(cursor.getString(3))
                        .setArtistId(cursor.getInt(4))
                        .setArtist(cursor.getString(5))
                        .setDuration(cursor.getLong(6))
                        .setPath(cursor.getString(7))
                        .setDateAdded(cursor.getLong(8))
                        .build();
                songs.add(song);
            }

            cursor.close();
        }

        return songs;
    }

    @Override
    public List<Song> fetchSongsFromArtist(Context context, int artistId) {
        List<Song> songs = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Projections.SONG,
                MediaStore.Audio.Media.ARTIST_ID + " = ?",
                new String[artistId],
                MediaStore.Audio.AudioColumns.TITLE + " ASC"
        );

        if (cursor != null) {
            Song song;
            while (cursor.moveToNext()) {
                song = new Song.Builder()
                        .setId(cursor.getInt(0))
                        .setTitle(cursor.getString(1))
                        .setAlbumId(cursor.getInt(2))
                        .setAlbum(cursor.getString(3))
                        .setArtistId(cursor.getInt(4))
                        .setArtist(cursor.getString(5))
                        .setDuration(cursor.getLong(6))
                        .setPath(cursor.getString(7))
                        .setDateAdded(cursor.getLong(8))
                        .build();
                songs.add(song);
            }

            cursor.close();
        }

        return songs;
    }

    @Override
    public List<Song> fetchSongsFromGenre(Context context, int genreId) {
        List<Song> songs = new ArrayList<>();

        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

        Cursor cursor = context.getContentResolver().query(
                uri,
                Projections.SONG,
                null,
                null,
                MediaStore.Audio.AudioColumns.TITLE + " ASC"
        );

        if (cursor != null) {
            Song song;
            while (cursor.moveToNext()) {
                song = new Song.Builder()
                        .setId(cursor.getInt(0))
                        .setTitle(cursor.getString(1))
                        .setAlbumId(cursor.getInt(2))
                        .setAlbum(cursor.getString(3))
                        .setArtistId(cursor.getInt(4))
                        .setArtist(cursor.getString(5))
                        .setDuration(cursor.getLong(6))
                        .setPath(cursor.getString(7))
                        .setDateAdded(cursor.getLong(8))
                        .build();
                songs.add(song);
            }

            cursor.close();
        }

        return songs;
    }

    @Override
    public List<Song> fetchSongsFromPlaylist(Context context, int playlistId) {
        List<Song> songs = new ArrayList<>();

        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);

        Cursor cursor = context.getContentResolver().query(
                uri,
                Projections.SONG,
                null,
                null,
                MediaStore.Audio.AudioColumns.TITLE + " ASC");

        if (cursor != null) {
            Song song;
            while (cursor.moveToNext()) {
                song = new Song.Builder()
                        .setId(cursor.getInt(0))
                        .setTitle(cursor.getString(1))
                        .setAlbumId(cursor.getInt(2))
                        .setAlbum(cursor.getString(3))
                        .setArtistId(cursor.getInt(4))
                        .setArtist(cursor.getString(5))
                        .setDuration(cursor.getLong(6))
                        .setPath(cursor.getString(7))
                        .setDateAdded(cursor.getLong(8))
                        .build();
                songs.add(song);
            }

            cursor.close();
        }

        return songs;

    }

    @Override
    public List<Album> getAlbumsFromArtist(Context context, int artistId) {
        List<Album> albums = new ArrayList<>();

        Cursor c1 = context.getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Artists._ID, MediaStore.Audio.ArtistColumns.ARTIST},
                MediaStore.Audio.Artists._ID + "= ?",
                new String[]{String.valueOf(artistId)},
                null
        );

        String artist;
        if (c1 != null) {
            artist = c1.getString(1);
            c1.close();
        } else {
            return albums;
        }

        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Projections.ALBUM,
                MediaStore.Audio.AlbumColumns.ARTIST + " = ?",
                new String[]{artist},
                null
        );

        if (c != null) {
            Album album;
            while (c.moveToNext()) {
                album = new Album.Builder()
                        .setAlbumId(c.getInt(0))
                        .setAlbumTitle(c.getString(1))
                        .setAlbumArt(c.getString(2))
                        .setArtist(c.getString(3))
                        .setNumberOfSongs(c.getInt(4))
                        .build();
                cachedAlbumList.add(album);
            }

            c.close();
        }

        return cachedAlbumList;
    }

    @Override
    public void clearCache() {
        cachedSongList = null;
        cachedAlbumList = null;
        cachedArtistList = null;
        cachedGenreList = null;
    }

    private Cursor getSongCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // user space media
                Projections.SONG, // columns from MediaStore database
                null,
                null,
                MediaStore.Audio.AudioColumns.TITLE + " ASC" // sort by title ascending
        );
    }

    private Cursor getAlbumCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Projections.ALBUM,
                null,
                null,
                MediaStore.Audio.AlbumColumns.ALBUM + " ASC"
        );
    }

    private Cursor getArtistCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                Projections.ARTIST,
                null,
                null,
                MediaStore.Audio.ArtistColumns.ARTIST + " ASC"
        );
    }

    private Cursor getGenreCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                Projections.GENRE,
                null,
                null,
                MediaStore.Audio.GenresColumns.NAME + " ASC"
        );
    }

    private Cursor getPlaylistCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                Projections.PLAYLIST,
                null,
                null,
                MediaStore.Audio.GenresColumns.NAME + " ASC"
        );
    }
}

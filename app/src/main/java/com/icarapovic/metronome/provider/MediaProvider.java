package com.icarapovic.metronome.provider;

import android.content.Context;

import com.icarapovic.metronome.models.Album;
import com.icarapovic.metronome.models.Artist;
import com.icarapovic.metronome.models.Genre;
import com.icarapovic.metronome.models.Playlist;
import com.icarapovic.metronome.models.Song;

import java.util.List;

import io.reactivex.Observer;

public interface MediaProvider {

    void fetchSongs(Context context, Observer<List<Song>> observer);

    void fetchArtists(Context context, Observer<List<Artist>> observer);

    void fetchAlbums(Context context, Observer<List<Album>> observer);

    void fetchGenres(Context context, Observer<List<Genre>> observer);

    void fetchPlaylists(Context context, Observer<List<Playlist>> observer);

    void fetchSongsFromAlbum(Context context, int albumId, Observer<List<Song>> observer);

    void fetchSongsFromArtist(Context context, int artistId, Observer<List<Song>> observer);

    void fetchSongsFromGenre(Context context, int genreId, Observer<List<Song>> observer);

    void fetchSongsFromPlaylist(Context context, int playlistId, Observer<List<Song>> observer);

    void fetchAlbumsFromArtist(Context context, int artistId, Observer<List<Album>> observer);

    void clearCache();

}

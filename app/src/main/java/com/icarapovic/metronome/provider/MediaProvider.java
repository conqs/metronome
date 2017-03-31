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

    List<Song> fetchSongsFromAlbum(Context context, int albumId);

    List<Song> fetchSongsFromArtist(Context context, int artistId);

    List<Song> fetchSongsFromGenre(Context context, int genreId);

    List<Song> fetchSongsFromPlaylist(Context context, int playlistId);

    List<Album> getAlbumsFromArtist(Context context, int artistId);

    void clearCache();

}

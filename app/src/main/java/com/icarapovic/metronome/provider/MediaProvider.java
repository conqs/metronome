package com.icarapovic.metronome.provider;

import android.content.Context;

import com.icarapovic.metronome.models.Album;
import com.icarapovic.metronome.models.Artist;
import com.icarapovic.metronome.models.Genre;
import com.icarapovic.metronome.models.Song;

import java.util.List;

public interface MediaProvider {

    List<Song> fetchSongs(Context context);

    List<Artist> fetchArtists(Context context);

    List<Album> fetchAlbums(Context context);

    List<Genre> fetchGenres(Context context);

    List<Song> fetchSongsFromAlbum(Context context, int albumId);

    List<Song> fetchSongsFromArtist(Context context, int artistId);

    List<Song> fetchSongsFromGenre(Context context, int genreId);

    List<Album> getAlbumsFromArtist(Context context, int artistId);

    void clearCache();

}

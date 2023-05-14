package com.audiogalaxy.audiogalaxy.controller;


import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class PlaylistController {

    private PlaylistService playlistService;

    @Autowired
    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }


    /**
     * Create a playlist and return the created playlist
     *
     * @param playlist the object contains required data for creating a playlist
     * @return created playlist
     */
    @PostMapping(path = "/playlists/")
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        return playlistService.createPlayList(playlist);
    }

    /**
     * Return a list of playlists that belong to currently authenticated user
     *
     * @return a list of playlists
     */
    @GetMapping(path = "/playlists/")
    public List<Playlist> getPlaylists() {
        return playlistService.getPlaylists();
    }

    /**
     * Retrieves the list of songs associated with a playlist based on the provided playlist ID.
     *
     * @param playlistId The ID of the playlist for which to retrieve the songs.
     * @return The list of songs associated with the specified playlist.
     * @throws InformationNotFoundException if the playlist with the given ID is not found.
     */
    @GetMapping(path = "/playlists/{playlistId}/")
    public List<Song> getSongsByPlaylist(@PathVariable Long playlistId) throws InformationNotFoundException {
        return playlistService.getSongByPlaylistId(playlistId);
    }

    /**
     * <<<<<<< HEAD
     * Calls method to delete specific playlist of currently logged-in user
     *
     * @param playlistId The id of playlist wanting to delete.
     * @return Deleted playlist
     * @throws InformationNotFoundException If an error occurs.
     */
    @DeleteMapping(path = "/playlists/{playlistId}/")
    public Playlist deletePlaylist(@PathVariable Long playlistId) throws InformationNotFoundException {
        return playlistService.deletePlaylistId(playlistId);
    }

    /**
     * Adds a song to a playlist based on the provided playlist ID and song.
     * <p>
     * Accepts a POST request with a JSON payload containing the song details. The method delegates the task of adding the
     * song to the playlist to the playlistService's addSongToPlaylist method, passing the playlist ID and the song object.
     *
     * @param playlistId The ID of the playlist to which the song will be added.
     * @param song       The Song object containing the details of the song to be added.
     * @return The updated Playlist object after adding the song.
     */
    @PostMapping(path = "/playlists/{playlistId}/songs/")
    public Playlist addSongToPlaylist(@PathVariable Long playlistId, @RequestBody Song song) {
        return playlistService.addSongToPlaylist(playlistId, song);
    }
}

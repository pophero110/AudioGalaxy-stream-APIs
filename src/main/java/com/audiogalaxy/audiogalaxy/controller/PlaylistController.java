package com.audiogalaxy.audiogalaxy.controller;


import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param playlist the object contains required data for creating a playlist
     * @return created playlist
     */
    @PostMapping(path = "/playlists/")
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        return playlistService.createPlayList(playlist);
    }
}

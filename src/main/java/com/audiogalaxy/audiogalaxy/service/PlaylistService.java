package com.audiogalaxy.audiogalaxy.service;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import com.audiogalaxy.audiogalaxy.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private UserContext userContext;

    @Autowired
    private void setPlaylistRepository(PlaylistRepository playlistRepository, UserContext userContext) {
        this.playlistRepository = playlistRepository;
        this.userContext = userContext;
    }

    /**
     * create a playlist in database and return the created playlist
     * @param playlist the object that contains required data for creating a playlist
     * @return created playlist
     * @throws InformationInvalidException if the name of playlist is blank
     */
    public Playlist createPlayList(Playlist playlist) {
        if (playlist.getName() == null || playlist.getName().isBlank()) {
            throw new InformationInvalidException("The name of playlist can not empty or contains only space");
        }
        playlist.setUser(userContext.getCurrentLoggedInUser());
        // saving the playlist to database
        return playlistRepository.save(playlist);
    }

    /**
     * fetch a list of playlists that belong to currently logged-in user and return the list
     * @return a list of playlists
     * @throws InformationInvalidException if the user does not have any playlist
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = playlistRepository.findByUserId(userContext.getCurrentLoggedInUser().getId());
        if (playlists.isEmpty()) {
            throw new InformationNotFoundException("No playlist has been found");
        }
        return playlists ;
    }
}


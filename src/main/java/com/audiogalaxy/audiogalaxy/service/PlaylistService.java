package com.audiogalaxy.audiogalaxy.service;


import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    private PlaylistRepository playlistRepository;

    @Autowired
    private void setPlaylistRepository(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }


    /**
     * create a playlist in database and return the created playlist
     * @param playlist the object that contains required data for creating a playlist
     * @return created playlist
     * @throws InformationInvalidException if the name of playlist is blank
     */
    public Playlist createPlayList(Playlist playlist) {
        if (playlist.getName() == null & playlist.getName().isBlank()) {
            throw new InformationInvalidException("The name of playlist can not empty or contains only space");
        }
        // saving the playlist to database
        return playlistRepository.save(playlist);
    }
}

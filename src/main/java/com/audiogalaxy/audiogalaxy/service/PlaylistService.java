package com.audiogalaxy.audiogalaxy.service;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import com.audiogalaxy.audiogalaxy.repository.SongRepository;
import com.audiogalaxy.audiogalaxy.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private UserContext userContext;

    private SongRepository songRepository;

    @Autowired
    private void beansInjection(PlaylistRepository playlistRepository, UserContext userContext, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.userContext = userContext;
        this.songRepository = songRepository;
    }

    /**
     * create a playlist in database and return the created playlist
     *
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
     *
     * @return a list of playlists
     * @throws InformationInvalidException if the user does not have any playlist
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = playlistRepository.findByUserId(userContext.getCurrentLoggedInUser().getId());
        if (playlists.isEmpty()) {
            throw new InformationNotFoundException("No playlist has been found");
        }
        return playlists;
    }


    /**
     * Retrieves the list of songs associated with a playlist based on the provided playlist ID.
     * <p>
     * Accepts a playlist ID and retrieves the corresponding Playlist entity from the playlist repository.
     * If the playlist is found, the method returns the list of songs associated with that playlist.
     * If the playlist is not found, an InformationNotFoundException is thrown with an appropriate error message.
     *
     * @param playlistId The ID of the playlist for which to retrieve the songs.
     * @return The list of songs associated with the specified playlist.
     * @throws InformationNotFoundException if the playlist with the given ID is not found.
     */
    public List<Song> getSongByPlaylistId(Long playlistId) {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        if (playlist.isPresent()) {
            return playlist.get().getSongs();
        } else {
            throw new InformationNotFoundException("Playlist with id " + playlistId + " is not found");
        }
    }

    /**
     * Deletes a playlist based on the provided playlist ID.
     *
     * Retrieves the playlist with the given ID and the ID of the currently logged-in user. If the playlist is found and
     * belongs to the user, it is deleted from the repository. The deleted playlist is returned.
     *
     * @param playlistId The ID of the playlist to be deleted.
     * @return The deleted playlist.
     * @throws InformationNotFoundException if the playlist with the given ID is not found or does not belong to the user.
     */
    public Playlist deletePlaylistId(Long playlistId) {
        Optional<Playlist> playlist = playlistRepository.findByIdAndUserId(playlistId, userContext.getCurrentLoggedInUser().getId());
        if (playlist.isPresent()) {
            playlistRepository.deleteById(playlistId);
            return playlist.get();
        } else {
            throw new InformationNotFoundException("User's Playlist with id " + playlistId + " is not found");
        }
    }

    /**
     * Adds a song to a playlist based on the provided playlist ID and song.
     * <p>
     * Checks if the currently logged-in user has the playlist with the given ID. If the playlist is found, it checks if the
     * song with the provided ID exists. If both the playlist and song are found, the song is added to the playlist, and the
     * updated playlist is saved.
     *
     * @param playlistId The ID of the playlist to which the song will be added.
     * @param song       The song to be added to the playlist.
     * @return The updated playlist after adding the song.
     * @throws InformationNotFoundException if the playlist with the given ID or the song with the given ID is not found.
     */
    public Playlist addSongToPlaylist(Long playlistId, Song song) {
        // check if the user has the playlist
        User currentUser = userContext.getCurrentLoggedInUser();
        Playlist foundPlaylist = playlistRepository.findByIdAndUserId(playlistId, currentUser.getId())
                .orElseThrow(() -> new InformationNotFoundException("Playlist with id " + playlistId + " is not found"));

        // check if the song exists
        Song foundSong = songRepository.findById(song.getId())
                .orElseThrow(() -> new InformationNotFoundException("Song with id " + song.getId() + " is not found"));

        foundPlaylist.getSongs().add(foundSong);
        return playlistRepository.save(foundPlaylist);
    }
}


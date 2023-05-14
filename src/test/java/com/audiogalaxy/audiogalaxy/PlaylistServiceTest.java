package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import com.audiogalaxy.audiogalaxy.repository.SongRepository;
import com.audiogalaxy.audiogalaxy.security.UserContext;
import com.audiogalaxy.audiogalaxy.service.PlaylistService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


@SpringBootTest
public class PlaylistServiceTest {

    @MockBean
    PlaylistRepository playlistRepository;

    @MockBean
    SongRepository songRepository;

    @MockBean
    UserContext userContext;

    @Autowired
    PlaylistService playlistService;


    @Test
    @DisplayName("save a playlist successfully")
    public void savePlaylistSuccessfully() {
        // Create a user and set it as the current logged-in user
        User loggedInUser = new User("jeff", "jeff@test.com", "123456");
        when(userContext.getCurrentLoggedInUser()).thenReturn(loggedInUser);

        // create a new playlist
        Playlist newPlaylist = new Playlist("rock music", "description");
        // mock the save method on playlistRepository
        when(playlistRepository.save(Mockito.any(Playlist.class)))
                .thenReturn(newPlaylist);

        // call the method under test
        Playlist createdPlaylist = playlistService.createPlayList(newPlaylist);

        // createPlaylist return a playlist
        Assertions.assertNotNull(createdPlaylist);
        // createdPlaylist should have the same data as the newPlaylist
        assertEquals(newPlaylist.getName(), createdPlaylist.getName());
        assertEquals(newPlaylist.getDescription(), createdPlaylist.getDescription());
        assertEquals(newPlaylist.getUser(), createdPlaylist.getUser());

        // should invoke the save method from playlistRepository
        verify(playlistRepository, times(1)).save(Mockito.any(Playlist.class));
        // should invoke the getCurrentLoggedInUser from userContext
        verify(userContext, times(1)).getCurrentLoggedInUser();
    }

    @Test
    @DisplayName("The name of playlist can not blank, otherwise an exception will be thrown")
    public void nameCanNotBeEmpty() {
        Playlist playlist = new Playlist("", "description");
        // mock the save method from playlistRepository
        when(playlistRepository.save(Mockito.any(Playlist.class)))
                .thenThrow(new InformationInvalidException("The name of playlist can not empty or contains only space"));

        Assert.assertThrows(InformationInvalidException.class, () -> playlistService.createPlayList(playlist));
    }

    @Test
    @DisplayName("get a list of playlists that belongs to currently logged in user")
    public void testGetPlaylists() {
        // set currently logged in user
        User currentlyLoggedInUser = new User("jeff", "jeff@gmail.com", "password");
        currentlyLoggedInUser.setId(2L);
        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);
        // set a list of playlists that belong to currentlyLoggedInUser
        List<Playlist> playlistLists = new ArrayList<>(Arrays.asList(
                new Playlist("rock", "description", currentlyLoggedInUser), new Playlist("relax", "description", currentlyLoggedInUser)
        ));
        when(playlistRepository.findByUserId(anyLong())).thenReturn(playlistLists);

        // call the method under test
        List<Playlist> returnedPlaylist = playlistService.getPlaylists();

        Assertions.assertEquals(2, returnedPlaylist.size());
    }

    @Test
    @DisplayName("if currently logged in user does not have any playlist, an exception will be thrown")
    public void testGetPlaylistsThrowsException() {
        // set currently logged in user
        User currentlyLoggedInUser = new User("jeff", "jeff@gmail.com", "password");
        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);
        // set a list of playlists that belong to currentlyLoggedInUser
        List<Playlist> playlistLists = new ArrayList<>();
        when(playlistRepository.findByUserId(anyLong())).thenReturn(playlistLists);

        // call the method under test
        Assertions.assertThrows(InformationNotFoundException.class, () -> playlistService.getPlaylists());
    }

    @Test
    @DisplayName("return a list of songs that belong to a specific playlist")
    public void testGetSongsByPlaylistIdSuccessfully() {
        // set a playlist and add song to it
        Playlist playlist = new Playlist("rock music", "description");
        playlist.getSongs().add(new Song("Champion's Journey", "Champion"));
        playlist.getSongs().add(new Song("Champion's Journey", "Unstoppable Spirit"));

        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(playlist));

        List<Song> playlistSongs = playlistService.getSongByPlaylistId(2L);

        Assertions.assertEquals(2, playlistSongs.size());
    }

    @Test
    @DisplayName("throw an exception if the playlist is not found")
    public void testGetSongsByPlaylistIdUnsuccessfully() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(InformationNotFoundException.class, () -> playlistService.getSongByPlaylistId(2L));
    }

    @Test
    @DisplayName("return playlist that was deleted as informational")
    public void testDeletePlaylistSuccessfully() {
        User currentlyLoggedInUser = new User("jeff", "jeff@gmail.com", "password");
        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);

        Playlist playlist = new Playlist(1L, "jazz music", "jazzy description", currentlyLoggedInUser);

        when(playlistRepository.findByIdAndUserId(playlist.getId(), currentlyLoggedInUser.getId())).thenReturn(Optional.of(playlist));

        Playlist actualPlaylist = playlistService.deletePlaylistId(playlist.getId());

        willDoNothing().given(playlistRepository).deleteById(playlist.getId());

        verify(playlistRepository, times(1)).deleteById(playlist.getId());
        Assertions.assertNotNull(actualPlaylist);
    }

    @Test
    @DisplayName("return playlist that was deleted as informational")
    public void testDeletePlaylistUnsuccessfully() {
        User currentlyLoggedInUser = new User("jeff", "jeff@gmail.com", "password");
        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);

        Playlist playlist = new Playlist("jazz music", "jazzy description");

        when(playlistRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(playlist));

        Assertions.assertThrows(InformationNotFoundException.class, () -> playlistService.deletePlaylistId(playlist.getId()));
    }

    @Test
    @DisplayName("add a song to a playlist successfully")
    public void testAddSongToPlaylistSuccessfully() {
        User currentlyLoggedInUser = new User("tim", "tim@gmail.com", "123456");
        currentlyLoggedInUser.setId(1L);
        Playlist playlist = new Playlist(1L, "favorite songs", "description");
        Song addedSong = new Song(1L, "album", "Champion");

        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);
        when(playlistRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(playlist));
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(addedSong));
        when(playlistRepository.save(Mockito.any(Playlist.class))).thenReturn(playlist);

        Playlist songsList = playlistService.addSongToPlaylist(playlist.getId(), addedSong);

        Assertions.assertEquals(1, songsList.getSongs().size());
    }

    @Test
    @DisplayName("add a song to a playlist unsuccessfully when playlist is not found")
    public void testAddSongToPlaylistUnsuccessfullyWhenPlaylistIsNotFound() {
        User currentlyLoggedInUser = new User("tim", "tim@gmail.com", "123456");
        currentlyLoggedInUser.setId(1L);
        Playlist playlist = new Playlist(2L, "favorite songs", "description");
        currentlyLoggedInUser.getPlaylists().add(playlist);
        Song addedSong = new Song(1L, "album", "Champion");
        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);

        InformationNotFoundException exception = Assertions
                .assertThrows(InformationNotFoundException.class, () -> playlistService.addSongToPlaylist(1L, addedSong));
        String expectedMessage = "Playlist with id 1 is not found";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("add a song to a playlist unsuccessfully when song is not found")
    public void testAddSongToPlaylistUnsuccessfullyWhenSongIsNotFound() {
        User currentlyLoggedInUser = new User("tim", "tim@gmail.com", "123456");
        currentlyLoggedInUser.setId(1L);
        Playlist playlist = new Playlist(1L, "favorite songs", "description");
        Song addedSong = new Song(1L, "album", "Champion");

        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);
        when(playlistRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(playlist));

        InformationNotFoundException exception = Assertions
                .assertThrows(InformationNotFoundException.class, () -> playlistService.addSongToPlaylist(playlist.getId(), addedSong));
        String expectedMessage = "Song with id " + addedSong.getId() + " is not found";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("update the name of a playlist successfully")
    public void testUpdatePlaylistSuccessfully() {
        User currentlyLoggedInUser = new User("tim", "tim@gmail.com", "123456");
        currentlyLoggedInUser.setId(1L);
        Playlist playlist = new Playlist(1L, "favorite songs", "description");

        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);
        when(playlistRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(Mockito.any(Playlist.class))).thenReturn(playlist);

        Playlist playlistObject = new Playlist("chill song", "none");
        Playlist updatedPlaylist = playlistService.updatePlaylist(1L, playlistObject);
        Assertions.assertEquals(updatedPlaylist.getName(), playlistObject.getName());
        Assertions.assertEquals(updatedPlaylist.getDescription(), playlistObject.getDescription());
    }

    @Test
    @DisplayName("update the name of a playlist with blank value throw an exception")
    public void testUpdatePlaylistUnsuccessfully() {
        User currentlyLoggedInUser = new User("tim", "tim@gmail.com", "123456");
        currentlyLoggedInUser.setId(1L);
        Playlist playlist = new Playlist(1L, "favorite songs", "description");

        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);
        when(playlistRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(playlist));

        Playlist playlistObject = new Playlist("", "none");
        InformationInvalidException exception = Assertions
                .assertThrows(InformationInvalidException.class, () -> playlistService.updatePlaylist(1L, playlistObject));
        String expectedMessage = "The name of the playlist cannot be empty or contain only spaces";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("update the name of a playlist but playlist is not found")
    public void testUpdatePlaylistUnsuccessfullyWhenPlaylistIsNotFound() {
        User currentlyLoggedInUser = new User("tim", "tim@gmail.com", "123456");
        currentlyLoggedInUser.setId(1L);

        when(userContext.getCurrentLoggedInUser()).thenReturn(currentlyLoggedInUser);
        when(playlistRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        Playlist playlistObject = new Playlist("", "none");
        InformationNotFoundException exception = Assertions
                .assertThrows(InformationNotFoundException.class, () -> playlistService.updatePlaylist(1L, playlistObject));
        String expectedMessage = "Playlist with id 1 is not found";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
}
package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import com.audiogalaxy.audiogalaxy.security.MyUserDetails;
import com.audiogalaxy.audiogalaxy.security.MyUserDetailsService;
import com.audiogalaxy.audiogalaxy.security.UserContext;
import com.audiogalaxy.audiogalaxy.service.PlaylistService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class PlaylistServiceTest {

    @MockBean
    PlaylistRepository playlistRepository;

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
}
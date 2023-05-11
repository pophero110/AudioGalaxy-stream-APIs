package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
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
}
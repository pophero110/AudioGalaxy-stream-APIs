package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import com.audiogalaxy.audiogalaxy.service.PlaylistService;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;


@SpringBootTest
public class PlaylistServiceTest {

    @MockBean
    PlaylistRepository playlistRepository;

    @Autowired
    PlaylistService playlistService;

    @Test
    @DisplayName("save a playlist successfully")
    public void savePlaylistSuccessfully() {
        // Arrange
        // create a new playlist
        Playlist newPlaylist = new Playlist("rock music", "description");
        // mock the save method on playlistRepository
        when(playlistRepository.save(Mockito.any(Playlist.class)))
                .thenReturn(newPlaylist);

        // Act
        Playlist createdPlaylist = playlistService.createPlayList(newPlaylist);

        // Assert
        // createPlaylist return a playlist
        Assert.assertNotNull(createdPlaylist);
        // the createdPlaylist have the same data as the newPlaylist
        Assert.assertEquals(newPlaylist.getName(), createdPlaylist.getName());
        Assert.assertEquals(newPlaylist.getDescription(), createdPlaylist.getDescription());

        // createPlaylist method should only invoke the save method on playlistRepository once
        verify(playlistRepository, times(1)).save(Mockito.any(Playlist.class));
    }

    @Test
    @DisplayName("The name of playlist can not blank, otherwise an exception will be thrown")
    public void nameCanNotBeEmpty() {
        // Arrange
        Playlist playlist = new Playlist("", "description");
        when(playlistRepository.save(Mockito.any(Playlist.class)))
                .thenThrow(new InformationInvalidException("The name of playlist can not empty or contains only space"));
        // Act and Assert
        Assert.assertThrows(InformationInvalidException.class, () -> playlistService.createPlayList(playlist));
    }
}
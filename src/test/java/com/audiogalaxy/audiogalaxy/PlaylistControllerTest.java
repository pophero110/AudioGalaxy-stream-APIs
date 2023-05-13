package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.controller.PlaylistController;
import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.security.*;
import com.audiogalaxy.audiogalaxy.security.MyUserDetailsService;
import com.audiogalaxy.audiogalaxy.service.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaylistController.class)
@Import(TestSecurityConfiguration.class)
public class PlaylistControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private PlaylistService playlistService;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @MockBean
    private JWTUtils jwtUtils;

    Playlist playlist = new Playlist("rock", "my favorite rock musics");

    @Test
    @DisplayName("should return 200 and a playlist object")
    public void shouldCreatePlaylistSuccessfully() throws Exception {
        // mock createPlaylist method to return a playlist
        when(playlistService.createPlayList(any(Playlist.class))).thenReturn(playlist);

        // mock the POST request for creating a playlist
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/playlists/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value(playlist.getName()))
                .andExpect(jsonPath("$.description").value(playlist.getDescription()))
                .andDo(print());
    }

    @Test
    @DisplayName("should return 400 when the name of the playlist is blank")
    public void shouldCreatePlaylistNotSuccessfully() throws Exception {
        Playlist playlistWithBlankName = new Playlist("", "my favorite rock musics");
        // mock createPlaylist method to throw an exception
        when(playlistService.createPlayList(any(Playlist.class)))
                .thenThrow(new InformationInvalidException("The name of playlist can not empty or contains only space"));
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/playlists/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlistWithBlankName));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("should return 200 and a list of playlists from currently authenticated user")
    public void shouldGetPlaylistSuccessfully() throws Exception {
        User currentlyLoggedInUser = new User("jeff", "jeff@gmail.com", "password");
        List<Playlist> playlistLists = new ArrayList<>(Arrays.asList(
                new Playlist("rock", "description", currentlyLoggedInUser), new Playlist("relax", "description", currentlyLoggedInUser)
        ));
        when(playlistService.getPlaylists()).thenReturn(playlistLists);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/api/playlists/");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("should return 404 if currently authenticated user has no playlist")
    public void shouldGetPlaylistUnSuccessfully() throws Exception {
        when(playlistService.getPlaylists()).thenThrow(new InformationNotFoundException("No playlist has been found"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/api/playlists/");

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("return 200 and a list of songs that belong to a playlist")
    public void shouldGetSongsFromPlaylistSuccessfully() throws Exception {
        when(playlistService.getSongByPlaylistId(anyLong())).thenReturn(Arrays.asList(new Song("album", "song")));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/api/playlists/1/");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("return 404 if the playlist is not found")
    public void shouldGetSongsFromPlaylistUnsuccessfully() throws Exception {
        when(playlistService.getSongByPlaylistId(anyLong())).thenThrow(new InformationNotFoundException("Playlist with id 1 is not found"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/api/playlists/1/");

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("add a song to a playlist successfully and return the playlist")
    public void shouldAddSongToPlaylistSuccessfully() throws Exception {
        Playlist playlist = new Playlist(1L, "favorite music", "description");
        Song song = new Song(1L, "album", "Champion");
        playlist.getSongs().add(song);
        when(playlistService.addSongToPlaylist(anyLong(), Mockito.any(Song.class))).thenReturn(playlist);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/playlists/1/songs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value(playlist.getName()))
                .andExpect(jsonPath("$.songs", notNullValue()))
                .andExpect(jsonPath("$.songs.length()").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("add a song to a playlist unsuccessfully when playlist is not found")
    public void shouldAddSongToPlaylistUnsuccessfullyWhenPlaylistIsNotFound () throws Exception {
        Song song = new Song(1L, "album", "Champion");
        when(playlistService.addSongToPlaylist(anyLong(), Mockito.any(Song.class))).thenThrow(new InformationNotFoundException("Playlist with id 1L is not found"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/playlists/1/songs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}

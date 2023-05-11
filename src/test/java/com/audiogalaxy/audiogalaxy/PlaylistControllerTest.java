package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.controller.PlaylistController;
import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.security.*;
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

import static org.hamcrest.Matchers.notNullValue;
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
        // Arrange
        // mock createPlaylist method to return a playlist
        when(playlistService.createPlayList(Mockito.any(Playlist.class))).thenReturn(playlist);

        // mock the POST request for creating a playlist
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/playlists/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist));

        // Act & Assert
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
        // Arrange
        Playlist playlistWithBlankName = new Playlist("", "my favorite rock musics");
        // mock createPlaylist method to throw a exception
        when(playlistService.createPlayList(Mockito.any(Playlist.class)))
                .thenThrow(new InformationInvalidException("The name of playlist can not empty or contains only space"));
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/playlists/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlistWithBlankName));

        // Act & Assert
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}

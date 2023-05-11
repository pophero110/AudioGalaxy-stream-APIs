package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.controller.SongController;
import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.repository.SongRepository;
import com.audiogalaxy.audiogalaxy.security.JWTUtils;
import com.audiogalaxy.audiogalaxy.security.JwtRequestFilter;
import com.audiogalaxy.audiogalaxy.security.MyUserDetailsService;
import com.audiogalaxy.audiogalaxy.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;


@WebMvcTest(SongController.class)
@Import(TestSecurityConfiguratoin.class)
public class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MyUserDetailsService myUserDetailsService;

    @MockBean
    JWTUtils jwtUtils;

    @MockBean
    SongService songService;


    @Test
    @DisplayName("Should return 200 and a list of maximum of 10 songs")
    public void testGet50Songs() throws Exception {
        //Creates a list of songs to be returned by the repository
        List<Song> songs = new ArrayList<>(Arrays.asList(
                new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song()
        ));

        when(songService.getSongs()).thenReturn(songs);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/songs/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10));
    }

}

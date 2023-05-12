package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.repository.SongRepository;
import com.audiogalaxy.audiogalaxy.service.SongService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class SongServiceTest {
    @MockBean
    private SongRepository songRepository;

    @Autowired
    private SongService songService;

    @Test
    @DisplayName("Return a list of most 10 songs")
    public void testGetSongs() {
        List<Song> songs = new ArrayList<>(Arrays.asList(
                new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song(),new Song()
        ));
        when(songRepository.findAll()).thenReturn(songs);
        List<Song> returnSongs = songService.getSongs();
        Assertions.assertEquals(10, returnSongs.size());
    }
}

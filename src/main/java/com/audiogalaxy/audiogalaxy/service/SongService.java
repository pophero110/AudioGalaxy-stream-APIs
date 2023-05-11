package com.audiogalaxy.audiogalaxy.service;

import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {
    private SongRepository songRepository;

    @Autowired
    public void setSongRepository(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    /**
     * Retrieves a list of 10 longs
     * This method fetches from the song repository and returns a filtered list
     * containing maximum of 10 songs
     * @return a list of songs, limited to maximum of 10 songs.
     */
    public List<Song> getSongs() {
        List<Song> songs = songRepository.findAll();
        List<Song> filteredSongs = songs.stream()
                .limit(10)
                .collect(Collectors.toList());
        return filteredSongs;
    }

}

package com.audiogalaxy.audiogalaxy.controller;

import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class SongController {

    SongService songService;

    @Autowired
    public void setSongService(SongService songService) {
        this.songService = songService;
    }

    /**
     * Retrieves a list of songs.
     * This endpoint accepts a GET request and retrieves a list of songs from the song service.
     * The method delegates the task to the songService's getSongs method and returns the list of songs.
     *
     * @return A list of songs.
     */
    @GetMapping(path = "/songs/")
    public List<Song> getSongs() {
        return songService.getSongs();
    }
}

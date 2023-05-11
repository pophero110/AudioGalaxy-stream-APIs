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

    @Autowired
    SongService songService;

    @GetMapping(path = "/songs/")
    public List<Song> getSongs() {
        return songService.getSongs();
    }


}

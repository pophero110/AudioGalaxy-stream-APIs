package com.audiogalaxy.audiogalaxy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
  
    // avoid JAP session expiration by fetching eagerly
    // com.audiogalaxy.audiogalaxy.service.PlaylistService.addSongToPlaylist:104
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs = new ArrayList<>();


    public Playlist() {

    }

    public Playlist(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Playlist(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public Playlist(Long id, String name, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    public List<Song> getSongs() {
        return songs;

    }

    public void addSong(Song song) {
        songs.add(song);
    }

}

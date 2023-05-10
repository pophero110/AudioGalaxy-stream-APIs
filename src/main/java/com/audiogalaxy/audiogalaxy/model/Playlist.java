package com.audiogalaxy.audiogalaxy.model;

import javax.persistence.*;

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
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

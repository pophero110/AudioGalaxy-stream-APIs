package com.audiogalaxy.audiogalaxy.model;


import javax.persistence.*;

@Entity
@Table(name="songs")
public class Song {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String album_name;

    @Column
    private String title;

    public Song() {
    }

    public Song(Long id, String album_name, String title) {
        this.id = id;
        this.album_name = album_name;
        this.title = title;
    }

    public Song(String album_name, String title) {
        this.album_name = album_name;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum_name() {
        return album_name;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", album_name='" + album_name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

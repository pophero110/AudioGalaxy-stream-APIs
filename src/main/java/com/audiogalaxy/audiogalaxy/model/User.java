package com.audiogalaxy.audiogalaxy.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Playlist> playlists;

    public User() {
    }

    public User(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}

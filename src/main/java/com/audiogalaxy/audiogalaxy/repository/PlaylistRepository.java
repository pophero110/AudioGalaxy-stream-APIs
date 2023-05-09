package com.audiogalaxy.audiogalaxy.repository;


import com.audiogalaxy.audiogalaxy.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}

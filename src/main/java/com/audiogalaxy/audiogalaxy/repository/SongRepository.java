package com.audiogalaxy.audiogalaxy.repository;


import com.audiogalaxy.audiogalaxy.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {


}

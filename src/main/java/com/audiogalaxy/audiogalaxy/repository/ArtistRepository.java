package com.audiogalaxy.audiogalaxy.repository;

import com.audiogalaxy.audiogalaxy.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

}

package com.audiogalaxy.audiogalaxy.seed;

import com.audiogalaxy.audiogalaxy.model.Artist;
import com.audiogalaxy.audiogalaxy.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ArtistDataLoader implements CommandLineRunner {

    @Autowired
    ArtistRepository artistRepository;

    @Override
    public void run(String... args) throws Exception {
        loadArtistData();
    }

    private void loadArtistData() {
        if (artistRepository.count() == 0) {
            Artist artist1 = new Artist("Taylor Swift");
            Artist artist2 = new Artist("Morgan Wallen");
            Artist artist3 = new Artist("AgustD");
            Artist artist4 = new Artist("Luke Combs");
            Artist artist5 = new Artist("SZA");
            Artist artist6 = new Artist("The Weekend");
            Artist artist7 = new Artist("Drake");
            Artist artist8 = new Artist("Milley Cyrus");
            Artist artist9 = new Artist("Metallica");
            Artist artist10 = new Artist("Ed Sheeran");
            artistRepository.save(artist1);
            artistRepository.save(artist2);
            artistRepository.save(artist3);
            artistRepository.save(artist4);
            artistRepository.save(artist5);
            artistRepository.save(artist6);
            artistRepository.save(artist7);
            artistRepository.save(artist8);
            artistRepository.save(artist9);
            artistRepository.save(artist10);
        }

    }
}

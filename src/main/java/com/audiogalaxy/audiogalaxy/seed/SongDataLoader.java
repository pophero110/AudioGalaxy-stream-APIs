package com.audiogalaxy.audiogalaxy.seed;

import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SongDataLoader  implements CommandLineRunner {

   @Autowired
    SongRepository songRepository;

    @Override
    public void run(String... args) throws Exception {
        loadSongData();
    }

    private void loadSongData() {
        if (songRepository.count() == 0) {
            Song song1 = new Song("Fearless", "Love Story");
            Song song2 = new Song("One Thing at a Time", "Last Night");
            Song song3 = new Song("D-Day", "Snooze");
            Song song4 = new Song("Gettin' Old", "Love You Anyway");
            Song song5 = new Song("SOS", "Open Arms");
            Song song6 = new Song("After Hours", "Faith");
            Song song7 = new Song("Take Care", "Lord Knows");
            Song song8 = new Song("Plastic Hearts", "Never Be Me");
            Song song9 = new Song("72 Seasons", "Too Far Gone?");
            Song song10 = new Song("Shades of You", "Perfect");
            songRepository.save(song1);
            songRepository.save(song2);
            songRepository.save(song3);
            songRepository.save(song4);
            songRepository.save(song5);
            songRepository.save(song6);
            songRepository.save(song7);
            songRepository.save(song8);
            songRepository.save(song9);
            songRepository.save(song10);
        }
    }
}

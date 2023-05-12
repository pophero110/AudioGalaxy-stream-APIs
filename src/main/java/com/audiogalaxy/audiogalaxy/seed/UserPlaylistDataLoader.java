package com.audiogalaxy.audiogalaxy.seed;

import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import com.audiogalaxy.audiogalaxy.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataLoader class for loading user and playlist data.
 * Implements the CommandLineRunner interface to execute the data loading process upon application startup.
 */
@Component
public class UserPlaylistDataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    /**
     * Executes the data loading process upon application startup.
     *
     * @param args Command line arguments
     * @throws Exception if an error occurs during the data loading process
     */
    @Override
    public void run(String... args) throws Exception {
            loadUserPlaylistData();
    }

    /**
     * Loads user and playlist data if the playlist repository is empty.
     */
    private void loadUserPlaylistData() {
        if (playlistRepository.count() == 0) {
            String password = passwordEncoder.encode("123456");
            User user1 = new User("Dan","dan@gmail.com", password );
            userRepository.save(user1);

            Playlist playlist1 = new Playlist("Rock music", "Rock of ages");
            playlist1.setUser(user1);
            playlistRepository.save(playlist1);

            Playlist playlist2 = new Playlist("Chill and Relax", "Sunday Morning by Maroon 5");
            playlist2.setUser(user1);
            playlistRepository.save(playlist2);

            Playlist playlist3 = new Playlist("Feel-Good Vibes", "Can't Stop the Feeling! by Justin Timberlake");
            playlist3.setUser(user1);
            playlistRepository.save(playlist3);

            User user2 = new User("Emily","emily@gmail.com", password );
            userRepository.save(user2);

            Playlist playlist4 = new Playlist("Alternative Hits", "Smells Like Teen Spirit by Nirvana");
            playlist4.setUser(user2);
            playlistRepository.save(playlist4);

            Playlist playlist5 = new Playlist("Throwback Classics", "I Will Always Love You by Whitney Houston");
            playlist5.setUser(user2);
            playlistRepository.save(playlist5);

            Playlist playlist6 = new Playlist("Energetic Pop", "Uptown Funk by Mark Ronson ft. Bruno Mars");
            playlist6.setUser(user2);
            playlistRepository.save(playlist6);


            User user3 = new User("Sally","sally@gmail.com", password );
            userRepository.save(user3);

            Playlist playlist7 = new Playlist("90s Hits", "No Diggity by Blackstreet ft. Dr. Dre");
            playlist7.setUser(user3);
            playlistRepository.save(playlist7);

            Playlist playlist8 = new Playlist("Indie Chill", "Ho Hey by The Lumineers");
            playlist8.setUser(user3);
            playlistRepository.save(playlist8);

            Playlist playlist9 = new Playlist("Motivation Mix", "Eye of the Tiger by Survivor");
            playlist9.setUser(user3);
            playlistRepository.save(playlist9);

        }
    }
}

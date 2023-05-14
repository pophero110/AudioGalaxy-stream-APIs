package com.audiogalaxy.audiogalaxy.seed;

import com.audiogalaxy.audiogalaxy.model.Playlist;
import com.audiogalaxy.audiogalaxy.model.Song;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.PlaylistRepository;
import com.audiogalaxy.audiogalaxy.repository.SongRepository;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * DataLoader class for loading user and playlist data.
 * Implements the CommandLineRunner interface to execute the data loading process upon application startup.
 */
@Component
public class UserPlaylistDataLoader implements CommandLineRunner {

    final
    UserRepository userRepository;

    final
    PlaylistRepository playlistRepository;

    final
    PasswordEncoder passwordEncoder;

    final
    SongRepository songRepository;

    @Autowired
    public UserPlaylistDataLoader(UserRepository userRepository, PlaylistRepository playlistRepository, PasswordEncoder passwordEncoder, SongRepository songRepository) {
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.passwordEncoder = passwordEncoder;
        this.songRepository = songRepository;
    }


    /**
     * Executes the data loading process upon application startup.
     *
     * @param args Command line arguments
     */
    @Override
    public void run(String... args) {
        loadUserPlaylistData();
    }

    /**
     * Loads user and playlist data if the playlist repository is empty.
     */
    private void loadUserPlaylistData() {
        if (playlistRepository.count() == 0) {

            //Create user & playlist objects
            String password = passwordEncoder.encode("123456");
            User user1 = new User("Dan", "dan@gmail.com", password);
            userRepository.save(user1);

            Playlist playlist1 = new Playlist("Rock music", "Rock of ages");
            playlist1.setUser(user1);
            playlistRepository.save(playlist1);

            Playlist playlist2 = new Playlist("Chill and Relax", "Sunday Morning by Maroon 5");
            playlist2.setUser(user1);
            playlistRepository.save(playlist2);

            Playlist playlist3 = new Playlist("Feel-Good Vibes", "Can't Stop the Feeling! by Justin Timberland");
            playlist3.setUser(user1);
            playlistRepository.save(playlist3);

            User user2 = new User("Emily", "emily@gmail.com", password);
            userRepository.save(user2);

            Playlist playlist4 = new Playlist("Alternative Hits", "Smells Like Teen Spirit by Nirvana");
            playlist4.setUser(user2);
            playlistRepository.save(playlist4);

            Playlist playlist5 = new Playlist("Throwback Classics", "I Will Always Love You by Whitney Houston");
            playlist5.setUser(user2);
            playlistRepository.save(playlist5);

            Playlist playlist6 = new Playlist("Energetic Pop", "Uptown Funk by Mark Robinson ft. Bruno Mars");
            playlist6.setUser(user2);
            playlistRepository.save(playlist6);


            User user3 = new User("Sally", "sally@gmail.com", password);
            userRepository.save(user3);

            Playlist playlist7 = new Playlist("90s Hits", "No Dignity by Backstreet ft. Dr. Dre");
            playlist7.setUser(user3);
            playlistRepository.save(playlist7);

            Playlist playlist8 = new Playlist("Indie Chill", "Ho Hey by The Illumines");
            playlist8.setUser(user3);
            playlistRepository.save(playlist8);

            Playlist playlist9 = new Playlist("Motivation Mix", "Eye of the Tiger by Survivor");
            playlist9.setUser(user3);
            playlistRepository.save(playlist9);

            //Create Songs
            Song song1 = new Song("Fearless", "Love Story");
            songRepository.save(song1);

            Song song2 = new Song("One Thing at a Time", "Last Night");
            songRepository.save(song2);

            Song song3 = new Song("D-Day", "Snooze");
            songRepository.save(song3);


            // Associate songs with playlists
            playlist1.addSong(song1);
            playlist1.addSong(song2);
            playlistRepository.save(playlist1);

            playlist2.addSong(song2);
            playlist2.addSong(song3);
            playlistRepository.save(playlist2);

            playlist3.addSong(song1);
            playlist3.addSong(song3);
            playlistRepository.save(playlist3);

            // Associate playlists with users
            List<Playlist> user1Playlists = new ArrayList<>();
            user1Playlists.add(playlist1);
            user1Playlists.add(playlist2);
            user1.setPlaylists(user1Playlists);
            userRepository.save(user1);

            List<Playlist> user2Playlists = new ArrayList<>();
            user2Playlists.add(playlist2);
            user2Playlists.add(playlist3);
            user2.setPlaylists(user2Playlists);
            userRepository.save(user2);

            List<Playlist> user3Playlists = new ArrayList<>();
            user3Playlists.add(playlist1);
            user3Playlists.add(playlist3);
            user3.setPlaylists(user3Playlists);
            userRepository.save(user3);

        }
    }
}

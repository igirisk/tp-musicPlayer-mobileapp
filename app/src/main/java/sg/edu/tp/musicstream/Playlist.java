package sg.edu.tp.musicstream;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Playlist {
    private ArrayList<Song> songs = new ArrayList<>();

    public int getRandomSong(int except) {
        int random = except;
        if (songs.size() == 1) {
            return except;
        }
        while (random == except) {
            random = ThreadLocalRandom.current().nextInt(songs.size());
        }
        return random;
    }

    public Playlist() {
    }

    public Playlist(int[] arr) {
        for (int i : arr) {
            songs.add(SongCollection.playlist.songs.get(i));
        }
    }

    public int[] indices() {
        int[] indices = new int[songs.size()];
        for (int i = 0; i < indices.length; ++i) {
            indices[i] = SongCollection.playlist.songs.indexOf(songs.get(i));
        }
        return indices;
    }

    public void add(Song song) {
        songs.add(song);
    }

    public boolean remove(Song song) {
        return songs.remove(song);
    }

    public int searchSongById(String id) {
        for (Song song : songs) {
            if (id.equals(song.getId())) {
                return SongCollection.playlist.songs.indexOf(song);
            }
        }
        return -1;
    }

    public int getPrevSong(int currentSongIndex) {
        if (currentSongIndex <= 0) {
            return currentSongIndex;
        } else {
            return currentSongIndex - 1;
        }
    }


    public int getNextSong(int currentSongIndex) {
        if (currentSongIndex >= songs.size() - 1) {
            return currentSongIndex;
        } else {
            return currentSongIndex + 1;
        }
    }

    public Song getCurrentSong(int currentSongId) {
        return songs.get(currentSongId);
    }


}
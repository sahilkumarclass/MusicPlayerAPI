package com.sahilkumar.musicplay;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

@RestController
public class MusicController {

    private List<String> songs = new ArrayList<>();
    private Clip currentClip;
    // fuction call for song name;
    public MusicController() {
        findFileNames();
    }

    @GetMapping("/api/music/songs")
    public List<String> getAllSongs() {
        return new ArrayList<>(songs);
    }
    //play song
    @GetMapping("/api/music/play/{index}")
    public String playSong(@PathVariable int index) {
        if (index >= 0 && index < songs.size()) {
            String filePath = "C:\\Users\\sahil\\OneDrive\\Desktop\\MusicPalyer\\musicAppBackend\\MusicPlay\\music\\" + songs.get(index);
            playMusic(filePath);
            return "Playing " + songs.get(index);
        } else {
            return "Invalid song index";
        }
    }
    //seaching function call
    @GetMapping("/api/music/search/{name}")
    public String searchSong(@PathVariable String name) {
        List<String> sortedSongs = new ArrayList<>(songs);
        Collections.sort(sortedSongs);
        int index = searchSong(sortedSongs, name);
        if (index >= 0) {
            playSong(index);
            return "Found " + name + " at index " + index;
        } else {
            return name + " not found";
        }
    }
    // sort song in descnding order
    @GetMapping("/api/music/sort")
    public List<String> sortSongs() {
        List<String> sortedSongs = new ArrayList<>(songs);
        Collections.sort(sortedSongs, Collections.reverseOrder());
        return sortedSongs;
    }
    // play next song;
    @GetMapping("/api/music/next/{currentIndex}")
    public String nextSong(@PathVariable int currentIndex) {
        int nextIndex = currentIndex + 1;
        if(nextIndex >= songs.size()) {
            nextIndex = 0;
        }
        return playSong(nextIndex);
//        if (nextIndex < songs.size()) {
//            return playSong(nextIndex);
//        } else {
//            return "No next song available";
//        }
    }
    // play previous song
    @GetMapping("/api/music/previous/{currentIndex}")
    public String previousSong(@PathVariable int currentIndex) {
        int previousIndex = currentIndex - 1;
        if (previousIndex < 0) {
            previousIndex = songs.size() - 1;
        }
        return playSong(previousIndex);
    }

//        if (previousIndex >= 0) {
//            return playSong(previousIndex);
//        } else {
//            return "No previous song available";
//        }
//    }
    //Stop Song
    @GetMapping("/api/music/stop")
    public String stopSong() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            return "Song stopped";
        } else {
            return "No song is playing";
        }
    }
    // Give name of all the song in directory;
    private void findFileNames() {
        Path dir = Paths.get("C:\\Users\\sahil\\OneDrive\\Desktop\\MusicPalyer\\musicAppBackend\\MusicPlay\\music");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                songs.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Searching Song
    private int searchSong(List<String> arr, String name) {
        int start = 0;
        int end = arr.size() - 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;
            int comparison = arr.get(mid).compareTo(name);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return -1; // not found
    }

    private void playMusic(String filePath) {
        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                if (currentClip != null && currentClip.isRunning()) {
                    currentClip.stop();
                }
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicPath);
                currentClip = AudioSystem.getClip();
                currentClip.open(audioInputStream);
                currentClip.start();
            } else {
                System.out.println("File does not exist");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

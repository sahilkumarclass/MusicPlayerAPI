package com.sahil.musicplyer.service;

import com.sahil.musicplyer.model.Song;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SongService {
    Song uploadSong(MultipartFile file, String title, String artist);
    List<Song> getAllSongs();
    Optional<Song> getSongById(String id);
    Song updateSong(String id, Song updatedSong);
    void deleteSong(String id);
}

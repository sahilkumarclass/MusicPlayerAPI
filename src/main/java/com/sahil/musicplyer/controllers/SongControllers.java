package com.sahil.musicplyer.controllers;

import com.sahil.musicplyer.model.Song;
import com.sahil.musicplyer.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongControllers {

    private final SongService songService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadSong(@RequestParam("file") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("artist") String artist) {
        try {
            Song uploaded = songService.uploadSong(file, title, artist);
            return ResponseEntity.ok(uploaded);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload song: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) {
        return songService.getSongById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable String id,
                                           @RequestBody Song updatedSong) {
        try {
            Song song = songService.updateSong(id, updatedSong);
            return ResponseEntity.ok(song);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable String id) {
        try {
            songService.deleteSong(id);
            return ResponseEntity.ok("Song deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Failed to delete song: " + e.getMessage());
        }
    }
}

package com.sahil.musicplyer.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sahil.musicplyer.model.Song;
import com.sahil.musicplyer.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongServiceImpl implements SongService {

    private final Cloudinary cloudinary;
    private final SongRepository songRepository;

    @Override
    @Transactional
    public Song uploadSong(MultipartFile file, String title, String artist) {
        if (file.isEmpty() || !file.getOriginalFilename().toLowerCase().endsWith(".mp3")) {
            throw new IllegalArgumentException("Only .mp3 files are allowed.");
        }
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto"
            ));

            Song song = Song.builder()
                    .fileName(file.getOriginalFilename())
                    .title(title)
                    .artist(artist)
                    .url((String) uploadResult.get("secure_url"))
                    .publicId((String) uploadResult.get("public_id"))
                    .isFavorite(false)
                    .build();

            return songRepository.save(song);
        } catch (IOException e) {
            log.error("Error uploading song to Cloudinary", e);
            throw new RuntimeException("Failed to upload song to Cloudinary");
        }
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @Override
    public Optional<Song> getSongById(String id) {
        return songRepository.findById(id);
    }

    @Override
    @Transactional
    public Song updateSong(String id, Song updatedSong) {
        return songRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedSong.getTitle());
                    existing.setArtist(updatedSong.getArtist());
                    existing.setFavorite(updatedSong.isFavorite());
                    return songRepository.save(existing);
                })
                .orElseThrow(() -> {
                    log.warn("Attempted to update a non-existing song with id: {}", id);
                    return new RuntimeException("Song not found with id: " + id);
                });
    }

    @Override
    @Transactional
    public void deleteSong(String id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to delete a non-existing song with id: {}", id);
                    return new RuntimeException("Song not found with id: " + id);
                });

        String publicId = song.getPublicId();
        if (publicId == null || publicId.isEmpty()) {
            log.error("Public ID is missing for song with id: {}", id);
            throw new RuntimeException("Public ID is missing. Cannot delete from Cloudinary.");
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "video"));
            songRepository.deleteById(id);
        } catch (IOException e) {
            log.error("Failed to delete song from Cloudinary for id: {}", id, e);
            throw new RuntimeException("Failed to delete song from Cloudinary.");
        }
    }
}

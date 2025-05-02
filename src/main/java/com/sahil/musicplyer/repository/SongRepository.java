package com.sahil.musicplyer.repository;

import com.sahil.musicplyer.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepository extends MongoRepository<Song, String> {
}

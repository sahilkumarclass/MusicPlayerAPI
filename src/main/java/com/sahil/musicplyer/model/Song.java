package com.sahil.musicplyer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "songs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    @Id
    private String id;
    private String fileName;
    private String title;
    private String artist;
    private String url;
    private String publicId;
    private boolean isFavorite;
}

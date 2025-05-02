package com.sahil.musicplyer.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dosb5nf5y",
                "api_key", "941575454145194",
                "api_secret", "2M1diUi0amg63WHcfBS4M-HzeZI",
                "secure", true
        ));
    }
}

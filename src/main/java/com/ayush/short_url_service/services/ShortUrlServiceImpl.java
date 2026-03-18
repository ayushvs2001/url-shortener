package com.ayush.short_url_service.services;

import com.ayush.short_url_service.entities.ShortUrl;
import com.ayush.short_url_service.repositories.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    ShortUrlRepository shortUrlRepository;

    @Autowired
    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Override
    public Optional<List<ShortUrl>> publicShortUrls() {
        return shortUrlRepository.findByIsPrivateFalse();
    }
}

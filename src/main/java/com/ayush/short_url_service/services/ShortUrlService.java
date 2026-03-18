package com.ayush.short_url_service.services;

import com.ayush.short_url_service.entities.ShortUrl;

import java.util.List;
import java.util.Optional;

public interface ShortUrlService {

    public Optional<List<ShortUrl>> publicShortUrls();
}

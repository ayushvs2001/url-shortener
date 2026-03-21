package com.ayush.short_url_service.services;

import com.ayush.short_url_service.dto.ShortUrlDto;
import com.ayush.short_url_service.entities.ShortUrl;

import java.util.List;
import java.util.Optional;

public interface ShortUrlService {

    public List<ShortUrlDto> publicShortUrls();

    public ShortUrlDto findByShortKey(String shortKey);
}

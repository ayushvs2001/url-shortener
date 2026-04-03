package com.ayush.short_url_service.services;

import com.ayush.short_url_service.dto.command.CreateShortUrlCommand;
import com.ayush.short_url_service.dto.response.ShortUrlDto;

import java.util.List;

public interface ShortUrlService {

    public List<ShortUrlDto> publicShortUrls();

    public ShortUrlDto findByShortKey(String shortKey, Long userId);

    public ShortUrlDto save(CreateShortUrlCommand createShortUrlCommand);
}

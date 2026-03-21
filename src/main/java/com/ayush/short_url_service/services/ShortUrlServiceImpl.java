package com.ayush.short_url_service.services;

import com.ayush.short_url_service.dto.ShortUrlDto;
import com.ayush.short_url_service.entities.ShortUrl;
import com.ayush.short_url_service.exceptions.ShortUrlNotFoundException;
import com.ayush.short_url_service.mapper.ShortUrlMapper;
import com.ayush.short_url_service.repositories.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    ShortUrlRepository shortUrlRepository;
    ShortUrlMapper shortUrlMapper;

    @Autowired
    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, ShortUrlMapper shortUrlMapper) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlMapper = shortUrlMapper;
    }

    @Override
    public List<ShortUrlDto> publicShortUrls() {
        List<ShortUrl> shortUrls = shortUrlRepository.findByIsPrivateFalse();

        System.out.println("inside the list public short urls method..." + shortUrls);

        return shortUrls.stream()
                .map(shortUrlMapper::toShortUrlDto)
                .toList();
    }

    @Override
    public ShortUrlDto findByShortKey(String shortKey) {
        Optional<ShortUrl> shortUrl = shortUrlRepository.findByShortKey(shortKey);
        if(shortUrl.isPresent()){
            return shortUrlMapper.toShortUrlDto(shortUrl.get());
        }

        throw new ShortUrlNotFoundException("Invalid Short URL: /" + shortKey);

    }
}

package com.ayush.short_url_service.services;

import com.ayush.short_url_service.config.ApplicationProperties;
import com.ayush.short_url_service.dto.command.CreateShortUrlCommand;
import com.ayush.short_url_service.dto.response.ShortUrlDto;
import com.ayush.short_url_service.entities.ShortUrl;
import com.ayush.short_url_service.exceptions.ShortUrlNotFoundException;
import com.ayush.short_url_service.mapper.ShortUrlMapper;
import com.ayush.short_url_service.repositories.ShortUrlRepository;
import com.ayush.short_url_service.validation.UrlValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlMapper shortUrlMapper;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, ShortUrlMapper shortUrlMapper, ApplicationProperties applicationProperties) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlMapper = shortUrlMapper;
        this.applicationProperties = applicationProperties;
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

            ShortUrl curShortUrl =  shortUrl.get();
            if(curShortUrl.getExpiresAt() != null && curShortUrl.getExpiresAt().isBefore(Instant.now())) {
                throw new ShortUrlNotFoundException("Short URL is expired: " + applicationProperties + "/" + shortKey);
            }

            curShortUrl.setClickCount(curShortUrl.getClickCount() + 1);
            shortUrlRepository.save(shortUrl.get());
            return shortUrlMapper.toShortUrlDto(shortUrl.get());
        }

        throw new ShortUrlNotFoundException("Invalid Short URL: /" + shortKey);

    }

    @Transactional
    @Override
    public ShortUrlDto save(CreateShortUrlCommand createShortUrlCommand)
    {
        if(applicationProperties.validateLongUrl()){
            if(!UrlValidator.isUrlExists(createShortUrlCommand.originalUrl()))
            {
                System.out.println("Long URL is not valid...");
                throw new RuntimeException("Provided Url is not valid " + createShortUrlCommand.originalUrl());
            }
        }

        String shortKey = generateUniqueShortKey();

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(createShortUrlCommand.originalUrl());
        shortUrl.setShortKey(shortKey);
        shortUrl.setClickCount(0L);
        shortUrl.setCreatedBy(null);
        shortUrl.setCreatedAt(Instant.now());
        shortUrl.setIsPrivate(false);
        shortUrl.setExpiresAt(Instant.now().plus(30, java.time.temporal.ChronoUnit.DAYS));
        shortUrlRepository.save(shortUrl);
        return shortUrlMapper.toShortUrlDto(shortUrl);
    }

    private String generateUniqueShortKey() {
        String shortKey;
         do {
            shortKey = generateRandomShortKey();
        } while (shortUrlRepository.existsByShortKey(shortKey));
        return shortKey;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_KEY_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}

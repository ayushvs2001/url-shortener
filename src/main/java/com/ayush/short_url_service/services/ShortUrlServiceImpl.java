package com.ayush.short_url_service.services;

import com.ayush.short_url_service.config.ApplicationProperties;
import com.ayush.short_url_service.dto.command.CreateShortUrlCommand;
import com.ayush.short_url_service.dto.response.ShortUrlDto;
import com.ayush.short_url_service.entities.ShortUrl;
import com.ayush.short_url_service.exceptions.ShortUrlNotFoundException;
import com.ayush.short_url_service.mapper.ShortUrlMapper;
import com.ayush.short_url_service.models.PagedResult;
import com.ayush.short_url_service.repositories.ShortUrlRepository;
import com.ayush.short_url_service.repositories.UserRepository;
import com.ayush.short_url_service.validation.UrlValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlMapper shortUrlMapper;
    private final ApplicationProperties applicationProperties;
    private final UserRepository userRepository;

    @Autowired
    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, ShortUrlMapper shortUrlMapper, ApplicationProperties applicationProperties, UserRepository userRepository) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlMapper = shortUrlMapper;
        this.applicationProperties = applicationProperties;
        this.userRepository = userRepository;
    }

    @Override
    public PagedResult<ShortUrlDto> publicShortUrls(Integer page, Integer pageSize) {
        int pageNo = page > 1 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ShortUrlDto> shortUrlsDto = shortUrlRepository.findAllPublicUrls(pageable)
                .map(shortUrlMapper::toShortUrlDto);

        System.out.println("inside the list public short urls method..." + shortUrlsDto);

        return PagedResult.from(shortUrlsDto);
    }

    @Override
    public ShortUrlDto findByShortKey(String shortKey, Long userId) {
        Optional<ShortUrl> shortUrl = shortUrlRepository.findByShortKey(shortKey);
        if(shortUrl.isPresent()){

            ShortUrl curShortUrl =  shortUrl.get();

            if(curShortUrl.getExpiresAt() != null && curShortUrl.getExpiresAt().isBefore(Instant.now())) {
                throw new ShortUrlNotFoundException("Short URL is expired: " + applicationProperties.baseUrl() + "/" + shortKey);
            }

            if(curShortUrl.getIsPrivate() != null && curShortUrl.getIsPrivate() && curShortUrl.getCreatedBy() != null && !Objects.equals(curShortUrl.getCreatedBy().getId(), userId))
            {
                throw new ShortUrlNotFoundException("Short URL is private: " + applicationProperties.baseUrl() + "/" + shortKey);
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
        if(createShortUrlCommand.userId() != null){
            shortUrl.setCreatedBy(userRepository.findById(createShortUrlCommand.userId()).orElse(null));
            shortUrl.setIsPrivate(createShortUrlCommand.isPrivate());
            Integer expiresInDays = createShortUrlCommand.expiresInDays() == null? applicationProperties.expiresInDays() : createShortUrlCommand.expiresInDays();
            shortUrl.setExpiresAt(Instant.now().plus(expiresInDays, java.time.temporal.ChronoUnit.DAYS));
        }
        else{
            shortUrl.setCreatedBy(null);
            shortUrl.setIsPrivate(false);
            shortUrl.setExpiresAt(Instant.now().plus(applicationProperties.expiresInDays(), java.time.temporal.ChronoUnit.DAYS));
        }

        shortUrl.setShortKey(shortKey);
        shortUrl.setClickCount(0L);
        shortUrl.setCreatedAt(Instant.now());
        shortUrlRepository.save(shortUrl);
        return shortUrlMapper.toShortUrlDto(shortUrl);
    }

    @Override
    public PagedResult<ShortUrlDto> getUserShortUrls(Long userId, Integer page, Integer pageSize) {
        int pageNo = page > 1 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ShortUrlDto> shortUrlsDto = shortUrlRepository.findByCreatedById(userId, pageable)
                .map(shortUrlMapper::toShortUrlDto);

        return PagedResult.from(shortUrlsDto);
    }

    @Transactional
    @Override
    public void deleteShortUrls(List<Long> ids, Long userId) {
        shortUrlRepository.deleteByIdInAndCreatedById(ids, userId);
    }

    @Override
    public PagedResult<ShortUrlDto> getAllShortUrls(Integer page, Integer pageSize) {
        int pageNo = page > 1 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ShortUrlDto> shortUrlsDto = shortUrlRepository.findAllShortUrls(pageable)
                .map(shortUrlMapper::toShortUrlDto);

        return PagedResult.from(shortUrlsDto);
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

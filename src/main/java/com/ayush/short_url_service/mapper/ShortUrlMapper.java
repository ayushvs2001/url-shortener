package com.ayush.short_url_service.mapper;

import com.ayush.short_url_service.dto.response.ShortUrlDto;
import com.ayush.short_url_service.dto.response.UserDto;
import com.ayush.short_url_service.entities.ShortUrl;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlMapper {

    public ShortUrlDto toShortUrlDto(ShortUrl shortUrl) {

        UserDto  userDto = null;
        if(shortUrl.getCreatedBy() != null){
            userDto = UserMapper.toUserDto(shortUrl.getCreatedBy());
        }

        ShortUrlDto shortUrlDto = new ShortUrlDto(
                                        shortUrl.getId(),
                                        shortUrl.getShortKey(),
                                        shortUrl.getOriginalUrl(),
                                        shortUrl.getExpiresAt(),
                                        userDto,
                                        shortUrl.getClickCount(),
                                        shortUrl.getCreatedAt()
                                    );

        return shortUrlDto;

    }
}

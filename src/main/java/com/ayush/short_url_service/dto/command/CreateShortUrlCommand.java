package com.ayush.short_url_service.dto.command;


import com.ayush.short_url_service.entities.User;

public record CreateShortUrlCommand(
        String originalUrl,
        Boolean isPrivate,
        Integer expiresInDays,
        Long userId
) {
}

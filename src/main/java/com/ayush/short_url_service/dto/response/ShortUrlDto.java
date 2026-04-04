package com.ayush.short_url_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShortUrlDto {
    private Long id;
    private String shortKey;
    private String originalUrl;
    private Instant expiresAt;
    private UserDto createdBy;
    private Long clickCount;
    private Instant createdAt;
    private Boolean isPrivate;
}

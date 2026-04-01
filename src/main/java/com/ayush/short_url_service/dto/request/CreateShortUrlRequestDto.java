package com.ayush.short_url_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateShortUrlRequestDto(
        @NotBlank(message = "Long URL is not valid")
        String originalUrl
) {
}

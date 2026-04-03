package com.ayush.short_url_service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateShortUrlRequestDto(
        @NotBlank(message = "Long URL is not valid")
        String originalUrl,
        Boolean isPrivate,
        @Min(1)
        @Max(365)
        Integer expiresInDays

) {
}

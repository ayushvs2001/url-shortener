package com.ayush.short_url_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDto(
        @NotBlank(message = "Email is required.")
        @Email(message = "Please enter valid email address.")
        String email,
        @NotBlank(message = "Name is required.")
        String name,
        @NotBlank(message =  "Password is required")
        String password
) {
}

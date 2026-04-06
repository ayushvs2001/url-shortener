package com.ayush.short_url_service.dto.command;

import com.ayush.short_url_service.enums.Role;

public record CreateUserCommand(
        String email,
        String name,
        String password,
        Role role
) {
}

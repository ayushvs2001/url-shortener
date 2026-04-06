package com.ayush.short_url_service.services;

import com.ayush.short_url_service.dto.command.CreateUserCommand;
import com.ayush.short_url_service.repositories.UserRepository;

public interface UserService {
    public void saveUser(CreateUserCommand createUserCommand);
}

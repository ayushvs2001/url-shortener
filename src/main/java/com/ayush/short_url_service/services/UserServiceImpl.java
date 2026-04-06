package com.ayush.short_url_service.services;

import com.ayush.short_url_service.dto.command.CreateUserCommand;
import com.ayush.short_url_service.entities.User;
import com.ayush.short_url_service.enums.Role;
import com.ayush.short_url_service.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void saveUser(CreateUserCommand createUserCommand) {
        if(userRepository.existsByEmail(createUserCommand.email()))
        {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(createUserCommand.email());
        user.setPassword(passwordEncoder.encode(createUserCommand.password()));
        user.setName(createUserCommand.name());
        user.setRole(createUserCommand.role());
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }
}

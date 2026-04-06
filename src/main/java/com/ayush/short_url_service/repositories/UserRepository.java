package com.ayush.short_url_service.repositories;

import com.ayush.short_url_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

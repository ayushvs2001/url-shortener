package com.ayush.short_url_service.repositories;

import com.ayush.short_url_service.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
     public Optional<List<ShortUrl>> findByIsPrivateFalse();

     public Optional<ShortUrl> findByShortKey(String shortKey);

}

package com.ayush.short_url_service.repositories;

import com.ayush.short_url_service.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    @Query("select su from ShortUrl su join fetch su.createdBy where su.isPrivate = false order by su.createdAt desc")
    public List<ShortUrl> findByIsPrivateFalse();

     public Optional<ShortUrl> findByShortKey(String shortKey);

}

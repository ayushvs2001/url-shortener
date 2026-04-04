package com.ayush.short_url_service.repositories;

import com.ayush.short_url_service.entities.ShortUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    @Query("select su from ShortUrl su left join fetch su.createdBy where su.isPrivate = false")
    public Page<ShortUrl> findAllPublicUrls(Pageable pageable);

     public Optional<ShortUrl> findByShortKey(String shortKey);

     public boolean existsByShortKey(String shortKey);

    public Page<ShortUrl> findByCreatedById(Long UserId, Pageable pageable);

    @Modifying
    public void deleteByIdInAndCreatedById(List<Long> ids, Long userId);

    @Query("select su from ShortUrl su left join fetch su.createdBy")
    public Page<ShortUrl> findAllShortUrls(Pageable pageable);
}

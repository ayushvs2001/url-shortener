package com.ayush.short_url_service.services;

import com.ayush.short_url_service.dto.command.CreateShortUrlCommand;
import com.ayush.short_url_service.dto.response.ShortUrlDto;
import com.ayush.short_url_service.models.PagedResult;

import java.util.List;

public interface ShortUrlService {

    public PagedResult<ShortUrlDto> publicShortUrls(Integer pageNo, Integer pageSize);

    public ShortUrlDto findByShortKey(String shortKey, Long userId);

    public ShortUrlDto save(CreateShortUrlCommand createShortUrlCommand);

    public PagedResult<ShortUrlDto> getUserShortUrls(Long userId, Integer pageNo, Integer pageSize);

    public void deleteShortUrls(List<Long> ids, Long userId);

    public PagedResult<ShortUrlDto> getAllShortUrls(Integer pageNo, Integer pageSize);

}

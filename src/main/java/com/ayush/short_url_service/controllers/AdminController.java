package com.ayush.short_url_service.controllers;

import com.ayush.short_url_service.config.ApplicationProperties;
import com.ayush.short_url_service.config.SecurityUtils;
import com.ayush.short_url_service.dto.response.ShortUrlDto;
import com.ayush.short_url_service.models.PagedResult;
import com.ayush.short_url_service.services.ShortUrlService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ShortUrlService shortUrlService;
    private final ApplicationProperties applicationProperties;

    public AdminController(ShortUrlService shortUrlService, ApplicationProperties applicationProperties, SecurityUtils securityUtils) {
        this.shortUrlService = shortUrlService;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(defaultValue = "1") Integer page,
                                 Model model) {
        PagedResult<ShortUrlDto> publicShortUrls = shortUrlService.getAllShortUrls(page, applicationProperties.pageSize());
        model.addAttribute("shortUrls", publicShortUrls);
        model.addAttribute("baseUrl", applicationProperties.baseUrl());
        model.addAttribute("paginationUrl", "/admin/dashboard");
        return "admin-dashboard";
    }

}

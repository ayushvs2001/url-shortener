package com.ayush.short_url_service.controllers;

import com.ayush.short_url_service.dto.ShortUrlDto;
import com.ayush.short_url_service.entities.ShortUrl;
import com.ayush.short_url_service.exceptions.ShortUrlNotFoundException;
import com.ayush.short_url_service.services.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    ShortUrlService shortUrlService;

    @Autowired
    public HomeController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ShortUrlDto> publicShortUrls = shortUrlService.publicShortUrls();

        model.addAttribute("publicShortUrls", publicShortUrls);
        model.addAttribute("baseUrl", "http://localhost:8080");

        return "homepage";
    }

    @GetMapping("/s/{shortKey}")
    public String redirectToShortUrl(@PathVariable String shortKey) {
        ShortUrlDto shortUrlDto = shortUrlService.findByShortKey(shortKey);

        return "redirect:"+shortUrlDto.getOriginalUrl();
    }

}

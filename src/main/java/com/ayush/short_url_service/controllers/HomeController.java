package com.ayush.short_url_service.controllers;

import com.ayush.short_url_service.entities.ShortUrl;
import com.ayush.short_url_service.services.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        Optional<List<ShortUrl>> publicShortUrls = shortUrlService.publicShortUrls();

        List<ShortUrl> currentPublicShortUrls = new ArrayList<>();

        if(publicShortUrls.isPresent()){
            currentPublicShortUrls = publicShortUrls.get();
        }

        model.addAttribute("publicShortUrls", currentPublicShortUrls);
        model.addAttribute("baseUrl", "http://localhost:8080");

        return "homepage";
    }

}

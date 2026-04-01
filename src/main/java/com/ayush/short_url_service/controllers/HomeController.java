package com.ayush.short_url_service.controllers;

import com.ayush.short_url_service.config.ApplicationProperties;
import com.ayush.short_url_service.config.SecurityUtils;
import com.ayush.short_url_service.config.WebSecurityConfig;
import com.ayush.short_url_service.dto.command.CreateShortUrlCommand;
import com.ayush.short_url_service.dto.request.CreateShortUrlRequestDto;
import com.ayush.short_url_service.dto.response.ShortUrlDto;
import com.ayush.short_url_service.services.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;
    private final ApplicationProperties applicationProperties;
    private final SecurityUtils securityUtils;

    @Autowired
    public HomeController(ShortUrlService shortUrlService, ApplicationProperties applicationProperties, SecurityUtils securityUtils) {
        this.shortUrlService = shortUrlService;
        this.applicationProperties = applicationProperties;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ShortUrlDto> publicShortUrls = shortUrlService.publicShortUrls();

        System.out.println("Security Utils " + securityUtils.getUserInfo());

        model.addAttribute("publicShortUrls", publicShortUrls);
        model.addAttribute("baseUrl", applicationProperties.baseUrl());

        return "homepage";
    }

    @GetMapping("/s/{shortKey}")
    public String redirectToShortUrl(@PathVariable String shortKey) {
        ShortUrlDto shortUrlDto = shortUrlService.findByShortKey(shortKey);

        return "redirect:"+shortUrlDto.getOriginalUrl();
    }

    @GetMapping("/show-create-short-url-form")
    public String showForm(Model model) {
        CreateShortUrlRequestDto createShortUrlRequestDto = new CreateShortUrlRequestDto("");
        model.addAttribute("createShortUrlForm", createShortUrlRequestDto);
        return "showForm";
    }

    @PostMapping("/short-urls")
    public String createShortUrls(
            @Valid @ModelAttribute("createShortUrlForm")  CreateShortUrlRequestDto createShortUrlRequestDto,
            BindingResult theBindingResult,
            RedirectAttributes redirectAttributes) {

            if(theBindingResult.hasErrors()) {
                return "showForm";
            }
            System.out.println("Form is processed");

            try{
                CreateShortUrlCommand createShortUrlCommand = new CreateShortUrlCommand(createShortUrlRequestDto.originalUrl());
                ShortUrlDto shortUrlDto = shortUrlService.save(createShortUrlCommand);
                redirectAttributes.addFlashAttribute("successMessage", "Form has been processed successfully. Short URL is created. - " + shortUrlDto.getShortKey());
            }
            catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Short URL creation has failed.");
                return "redirect:/show-create-short-url-form";
            }

            // redirect help to avoid populating same data, or duplicate data by refreshing page
            // To prevent duplicate form submission and follow PRG (Post/Redirect/Get) pattern.
            return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

}

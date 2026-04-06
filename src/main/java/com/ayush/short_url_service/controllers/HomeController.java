package com.ayush.short_url_service.controllers;

import com.ayush.short_url_service.config.ApplicationProperties;
import com.ayush.short_url_service.config.SecurityUtils;
import com.ayush.short_url_service.config.WebSecurityConfig;
import com.ayush.short_url_service.dto.command.CreateShortUrlCommand;
import com.ayush.short_url_service.dto.command.CreateUserCommand;
import com.ayush.short_url_service.dto.request.CreateShortUrlRequestDto;
import com.ayush.short_url_service.dto.request.CreateUserRequestDto;
import com.ayush.short_url_service.dto.response.ShortUrlDto;
import com.ayush.short_url_service.entities.User;
import com.ayush.short_url_service.enums.Role;
import com.ayush.short_url_service.exceptions.ShortUrlNotFoundException;
import com.ayush.short_url_service.models.PagedResult;
import com.ayush.short_url_service.services.ShortUrlService;
import com.ayush.short_url_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;
    private final UserService userService;
    private final ApplicationProperties applicationProperties;
    private final SecurityUtils securityUtils;

    @Autowired
    public HomeController(ShortUrlService shortUrlService, UserService userService, ApplicationProperties applicationProperties, SecurityUtils securityUtils) {
        this.shortUrlService = shortUrlService;
        this.userService = userService;
        this.applicationProperties = applicationProperties;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "1") Integer page,
            Model model) {
        PagedResult<ShortUrlDto> publicShortUrls = shortUrlService.publicShortUrls(page, applicationProperties.pageSize());
        model.addAttribute("shortUrls", publicShortUrls);
        model.addAttribute("baseUrl", applicationProperties.baseUrl());
        model.addAttribute("paginationUrl", "/");

        return "homepage";
    }

    @GetMapping("/s/{shortKey}")
    public String redirectToShortUrl(@PathVariable String shortKey) {
        Long userId = securityUtils.getUserId();
        ShortUrlDto shortUrlDto = shortUrlService.findByShortKey(shortKey, userId);
        return "redirect:"+shortUrlDto.getOriginalUrl();
    }

    @GetMapping("/show-create-short-url-form")
    public String showForm(Model model) {
        CreateShortUrlRequestDto form = new CreateShortUrlRequestDto("", false, null);
        model.addAttribute("createShortUrlForm", form);
        return "show-form";
    }

    @PostMapping("/short-urls")
    public String createShortUrls(
            @Valid @ModelAttribute("createShortUrlForm")  CreateShortUrlRequestDto form,
            BindingResult theBindingResult,
            RedirectAttributes redirectAttributes) {

            if(theBindingResult.hasErrors()) {
                return "showForm";
            }
            System.out.println("Form is processed");

            try{
                CreateShortUrlCommand createShortUrlCommand = new CreateShortUrlCommand(
                        form.originalUrl(),
                        form.isPrivate(),
                        form.expiresInDays(),
                        securityUtils.getUserId()
                );

                ShortUrlDto shortUrlDto = shortUrlService.save(createShortUrlCommand);
                redirectAttributes.addFlashAttribute("successMessage", "Form has been processed successfully. Short URL is created. - " + applicationProperties.baseUrl() + "/s/" +shortUrlDto.getShortKey());
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

    @GetMapping("/my-urls")
    public String getMyUrls(@RequestParam(defaultValue = "1") Integer page,
                            Model model) {
        Long userId = securityUtils.getUserId();
        PagedResult<ShortUrlDto> publicShortUrls = shortUrlService.getUserShortUrls(userId, page, applicationProperties.pageSize());
        model.addAttribute("shortUrls", publicShortUrls);
        model.addAttribute("baseUrl", applicationProperties.baseUrl());
        model.addAttribute("paginationUrl", "/my-urls");
        return "my-urls";
    }

    @PostMapping("/delete-urls")
    public String deleteUrls(@RequestParam(name = "ids", required = false) List<Long> ids,
                             RedirectAttributes redirectAttributes) {
        Long userId = securityUtils.getUserId();

        if(ids == null || ids.isEmpty() || userId == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Please select the URL/s.");
        }

        try{
            shortUrlService.deleteShortUrls(ids, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Url/s has been deleted successfully");
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Error in deleting URLs - " + e.getMessage());
        }
        return "redirect:/my-urls";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new CreateUserRequestDto("", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") CreateUserRequestDto userInfo,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "register";
        }

        try{
            CreateUserCommand createUserCommand = new CreateUserCommand(
                    userInfo.email(),
                    userInfo.name(),
                    userInfo.password(),
                    Role.ROLE_USER
            );
            userService.saveUser(createUserCommand);
            redirectAttributes.addFlashAttribute("successMessage", "User has been created successfully");
            return "redirect:/login";
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Error in creating user - " + e.getMessage());
            return "redirect:/register";
        }
    }


}

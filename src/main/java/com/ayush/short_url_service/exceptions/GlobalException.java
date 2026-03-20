package com.ayush.short_url_service.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public String handleNotFound(ShortUrlNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        System.out.println("Inside 404 exception handler");
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String generalException(Exception ex){
        System.out.println("Inside 500 exception handler");
        return "error/500";
    }
}


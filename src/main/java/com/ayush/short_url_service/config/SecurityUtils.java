package com.ayush.short_url_service.config;

import com.ayush.short_url_service.entities.User;
import com.ayush.short_url_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.extras.springsecurity6.util.SpringSecurityContextUtils;

@Service
public class SecurityUtils {

    UserRepository userRepository;

    @Autowired
    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!= null && authentication.isAuthenticated()){
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            return user;
        }

        return null;
    }
}

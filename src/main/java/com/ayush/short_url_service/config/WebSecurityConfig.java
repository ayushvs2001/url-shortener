package com.ayush.short_url_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                csrf(CsrfConfigurer::disable).
                authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/error", "/webjars/**", "/css/**", "/js/**", "/images/**",
                        "/", "/short-urls", "/s/**", "/register", "/login").permitAll().
                         requestMatchers("/my-urls").authenticated().
                        requestMatchers("/admin-dashboard").hasRole("ADMIN").
                        anyRequest().authenticated())
                .formLogin( formLogin -> formLogin.loginPage("/login").defaultSuccessUrl("/").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").permitAll().logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }



}

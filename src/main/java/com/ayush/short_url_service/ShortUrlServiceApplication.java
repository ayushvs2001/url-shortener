package com.ayush.short_url_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ShortUrlServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortUrlServiceApplication.class, args);
	}
}

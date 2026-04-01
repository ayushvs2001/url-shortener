package com.ayush.short_url_service.validation;

import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Component
public class UrlValidator {
    public static boolean isUrlExists(String urlString) {
        try {

//          Below line validate the URL
            URL url = new URI(urlString).toURL();

//          Below we are checking if the URL is reachable
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            return (responseCode >= 200 && responseCode < 400); // 2xx and 3xx are valid
        } catch (Exception e) {
            return false; // URL is invalid or not reachable
        }
    }

}

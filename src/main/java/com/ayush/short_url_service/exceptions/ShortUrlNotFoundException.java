package com.ayush.short_url_service.exceptions;

public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String message) {
        super(message);
    }

    public ShortUrlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShortUrlNotFoundException(Throwable cause) {
        super(cause);
    }
}

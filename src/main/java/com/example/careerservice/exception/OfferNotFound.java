package com.example.careerservice.exception;

import org.springframework.http.HttpStatus;

public class OfferNotFound extends AppException{
    private final String url;

    public OfferNotFound(String url) {
        super("Job offer not found or expired for url: " + url, HttpStatus.NOT_FOUND);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

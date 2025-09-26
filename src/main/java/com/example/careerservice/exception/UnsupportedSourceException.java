package com.example.careerservice.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UnsupportedSourceException extends AppException {
    private final List<String> supportedSources;

    public UnsupportedSourceException(String url, List<String> supportedSources) {
        super("No scrapper found for url: " + url
                + "\n Supported sources: " + String.join(", ", supportedSources),
            HttpStatus.BAD_REQUEST);
        this.supportedSources = supportedSources;
    }

    public List<String> getSupportedSources() {
        return supportedSources;
    }
}

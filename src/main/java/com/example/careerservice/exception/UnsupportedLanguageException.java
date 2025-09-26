package com.example.careerservice.exception;

import org.springframework.http.HttpStatus;

public class UnsupportedLanguageException extends AppException {
    public UnsupportedLanguageException(String language) {
        super("Unsupported language: " + language, HttpStatus.BAD_REQUEST);
    }
}

package com.example.careerservice.exception;

public class UnsupportedLanguageException extends RuntimeException {
    public UnsupportedLanguageException(String language) {
        super("Unsupported language: " + language);
    }
}

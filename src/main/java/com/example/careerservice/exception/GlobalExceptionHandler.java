package com.example.careerservice.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thymeleaf.exceptions.TemplateProcessingException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex
    ) {
        List<String> details = ex
            .getBindingResult()
            .getAllErrors()
            .stream()
            .map(this::formatValidationError)
            .collect(Collectors.toList());

        log.warn("Validation failed: {}", details);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
            ErrorResponse.of(status, "Validation failed", details)
        );
    }

    @ExceptionHandler(UnsupportedLanguageException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedLanguage(
        UnsupportedLanguageException ex
    ) {
        log.error("Unsupported language: {}", ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
            ErrorResponse.of(status, "Unsupported language", ex.getMessage())
        );
    }

    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTemplateNotFound(
        TemplateNotFoundException ex
    ) {
        log.error("Template not found: {}", ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
            ErrorResponse.of(status, "Template not found", ex.getMessage())
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex
    ) {
        if (ex.getCause() instanceof InvalidFormatException cause) {
            return handleInvalidFormatException(cause);
        }

        log.error("Message not readable: {}", ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
            ErrorResponse.of(status, "Invalid request payload", ex.getMessage())
        );
    }

    @ExceptionHandler(TemplateProcessingException.class)
    public ResponseEntity<ErrorResponse> handleTemplateProcessing(
        TemplateProcessingException ex
    ) {
        log.error("Template processing error: {}", ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
            ErrorResponse.of(
                status,
                "Template rendering error",
                "Failed to render template: " + ex.getMessage()
            )
        );
    }

//    @ExceptionHandler(ServletRequestBindingException.class)
//    public ResponseEntity<ErrorResponse> handleServletRequestBinding(ServletRequestBindingException ex) {
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        return ResponseEntity.status(status).body(
//            ErrorResponse.of(
//                status,
//                "Invalid or missing request data",
//                ex.getMessage()
//            )
//        );
//    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
//        log.error("Illegal argument: {}", ex.getMessage());
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        return ResponseEntity.status(status).body(
//            ErrorResponse.of(status, "Invalid argument", ex.getMessage())
//        );
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(
            ErrorResponse.of(
                status,
                "An unexpected error occurred",
                ex.getMessage()
            )
        );
    }

    // Helper methods
    private String formatValidationError(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            String field = fieldError.getField();
            // Simplify nested paths for better readability
            field = field.replace("personalInfo.", "");
            return field + ": " + fieldError.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }

    private ResponseEntity<ErrorResponse> handleInvalidFormatException(
        InvalidFormatException cause
    ) {
        if (cause.getTargetType().isEnum()) {
            String invalidValue = String.valueOf(cause.getValue());
            String acceptedValues = cause.getTargetType().getEnumConstants() !=
                null
                ? Arrays.stream(
                    cause.getTargetType().getEnumConstants()
                )
                    .map(Object::toString)
                    .collect(Collectors.joining(", "))
                : "";

            String detail = String.format(
                "Invalid value: '%s'. Accepted values are: [%s]",
                invalidValue,
                acceptedValues
            );

            log.warn(
                "Invalid enum value: {} for enum {}",
                invalidValue,
                cause.getTargetType().getSimpleName()
            );

            HttpStatus status = HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(
                ErrorResponse.of(status, "Invalid enum value", detail)
            );
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
            ErrorResponse.of(status, "Invalid format", cause.getMessage())
        );
    }
}

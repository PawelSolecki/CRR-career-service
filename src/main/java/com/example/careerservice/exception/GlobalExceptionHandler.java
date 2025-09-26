package com.example.careerservice.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Bean Validation + Spring Binding
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
        ServletRequestBindingException.class,
    })
    public ResponseEntity<ErrorResponse> handleValidation(Exception ex) {
        List<String> details = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException manve) {
            details = manve.getBindingResult().getAllErrors().stream()
                .map(this::formatValidationError)
                .toList();
        } else if (ex instanceof BindException be) {
            details = be.getBindingResult().getAllErrors().stream()
                .map(this::formatValidationError)
                .toList();
        } else if (ex instanceof ConstraintViolationException cve) {
            details = cve.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();
        }

        log.warn("Validation failed: {}", details);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
            .body(ErrorResponse.of(status, "Validation failed", details));
    }

    //Domain exceptions
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        log.warn("App exception: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
            .body(ErrorResponse.of(ex.getStatus(), ex.getClass().getSimpleName(), ex.getMessage()));
    }


    //JSON parse errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException cause && cause.getTargetType().isEnum()) {
            String invalidValue = String.valueOf(cause.getValue());
            String acceptedValues = Arrays.stream(cause.getTargetType().getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
            String detail = String.format("Invalid value: '%s'. Accepted values are: [%s]", invalidValue, acceptedValues);

            log.warn("Invalid enum value: {} for enum {}", invalidValue, cause.getTargetType().getSimpleName());
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                .body(ErrorResponse.of(status, "Invalid enum value", detail));
        }

        log.error("Invalid request payload: {}", ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
            .body(ErrorResponse.of(status, "Invalid request payload", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
            .body(ErrorResponse.of(status, "An unexpected error occurred", ex.getMessage()));
    }

    //helper
    private String formatValidationError(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            String field = fieldError.getField().replace("personalInfo.", "");
            return field + ": " + fieldError.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }
}

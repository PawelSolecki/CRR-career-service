package com.example.careerservice.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    List<String> details
) {
    public static ErrorResponse of(
            HttpStatus status,
            String message,
            List<String> details
    ) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                details
        );
    }

    // Convenience method for single detail message
    public static ErrorResponse of(
            HttpStatus status,
            String message,
            String detail
    ) {
        return of(status, message, Collections.singletonList(detail));
    }
}

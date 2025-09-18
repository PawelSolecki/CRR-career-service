package com.example.careerservice.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FilenameUtil {
    public static String getContentDispositionHeader(String firstName, String lastName) {
        // Create ASCII-only version for the standard filename parameter
        String asciiFilename = String.format("CV_%s_%s.pdf",
                removeNonAscii(firstName),
                removeNonAscii(lastName));

        // Create UTF-8 encoded version for the filename* parameter
        String originalFilename = String.format("CV_%s_%s.pdf", firstName, lastName);
        String encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8)
                .replace("+", "%20");

        return String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s",
                asciiFilename, encodedFilename);
    }

    private static String removeNonAscii(String input) {
        return input.replaceAll("[^\\x00-\\x7F]", "");
    }
}

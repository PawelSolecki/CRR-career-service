package com.example.careerservice.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator {
    public static void validate(String url, String expectedDomain) throws MalformedURLException {
        String domain = new URL(url).getHost().toLowerCase();
        String expected = expectedDomain.toLowerCase();
        if (!domain.equals(expected) && !domain.endsWith("." + expected)) {
            throw new IllegalArgumentException("URL domain does not match: " + expectedDomain);
        }
    }
}

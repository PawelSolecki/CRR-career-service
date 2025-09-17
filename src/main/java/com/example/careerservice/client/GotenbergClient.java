package com.example.careerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "gotenbergClient", url = "${gotenberg.api.url}")
public interface GotenbergClient {
    @PostMapping(
            value = "/forms/chromium/convert/html",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    ResponseEntity<byte[]> convertHtml(@RequestPart("files") MultipartFile file);
}
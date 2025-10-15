package com.example.careerservice.controller;

import com.example.careerservice.exception.ErrorResponse;
import com.example.careerservice.model.GeneratePdfRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "PDF", description = "Endpoints for PDF generation")
public interface IPdfController {

    @Operation(
            summary = "Generate PDF CV",
            description = "Generates a PDF CV from the provided data",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GeneratePdfRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF successfully generated. Returns a binary PDF file that can be downloaded.",
                            content = @Content(
                                    mediaType = "application/pdf",
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid input parameters",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resource not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/generate")
    ResponseEntity<Resource> generatePdf(
            @Valid @org.springframework.web.bind.annotation.RequestBody GeneratePdfRequest request
    );
}
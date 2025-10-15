package com.example.careerservice.controller;

import com.example.careerservice.exception.ErrorResponse;
import com.example.careerservice.model.PrepareCvRequest;
import com.example.careerservice.model.UserCV;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "CV", description = "Endpoints for CV preparation")
public interface ICvController {

    @Operation(
            summary = "Prepare CV by enhancing skills based on job description",
            description = "Enhances the user's CV by analyzing the provided job description and adjusting the CV's skills accordingly.",
            requestBody = @RequestBody(
                    description = "Data required to prepare the CV",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrepareCvRequest.class)
                    )
            ),
            parameters = {
                    @Parameter(
                            name = "topN",
                            description = "Number of top skills to prioritize. Default is -1 (no limit).",
                            schema = @Schema(type = "integer", example = "-1")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "CV successfully prepared",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserCV.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid input parameters",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/prepare")
    UserCV prepareCv(
            @org.springframework.web.bind.annotation.RequestBody @Valid PrepareCvRequest request,
            @RequestParam(defaultValue = "-1") int topN
    );
}
package com.example.careerservice.controller;

import com.example.careerservice.exception.ErrorResponse;
import com.example.careerservice.model.JobOffer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Scrapper", description = "Endpoints for scraping job offers")
public interface IScrapperController {

    @Operation(
            summary = "Scrape job offer",
            description = "Scrapes job offer data from the given URL.",
            parameters = @Parameter(
                    name = "url",
                    description = "URL of the job offer page",
                    required = true,
                    schema = @Schema(type = "string", format = "uri", example = "https://example.com/job/java-developer")
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Job offer successfully scraped",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JobOffer.class)
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
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    JobOffer scrape(@RequestParam String url);
}
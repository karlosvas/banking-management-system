package com.bytes.ms_customers.anotations;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.bytes.ms_customers.dtos.SwaggerErrorDTO;

/**
 * Annotation to define common API endpoint responses, managed by Swagger.
 * @Target indicates that this annotation can be applied to methods and classes.
 * @Retention indicates that this annotation is available at runtime.
 * @ApiResponses defines shared endpoint responses with status codes, descriptions, and response schemas.
 * @interface declares the SwaggerApiResponses annotation.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "405", description = "Method not allowed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "408", description = "Request timeout", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "502", description = "Bad gateway (Inter-service communication error)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "504", description = "Gateway timeout (The microservice took too long to respond)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class)))
})
public @interface SwaggerApiResponses {
}

package com.bytes.ms_accounts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration class.
 * Configures automatic API documentation using Swagger.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures general API documentation using OpenAPI.
     * Also configures the JWT security scheme (Bearer Token) for protected endpoints.
     *
     * @return an {@link OpenAPI} instance with API configuration.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Banking Management System") // API title
                        .version("1.0") // API version
                        .description("API documentation for the banking management system. Provides endpoints to manage customers, accounts, transactions, and more.")) // API description
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
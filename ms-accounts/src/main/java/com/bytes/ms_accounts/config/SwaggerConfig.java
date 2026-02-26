package com.bytes.ms_accounts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración de Swagger/OpenAPI.
 * Esta clase configura la documentación automática de la API utilizando Swagger.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura la documentación general de la API utilizando OpenAPI.
     * También configura el esquema de seguridad JWT (Bearer Token) que se utilizará para acceder a los endpoints protegidos.
     *
     * @return una instancia de {@link OpenAPI} con la configuración de la API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Banking Management System") // Título de la API
                        .version("1.0") // Versión de la API
                        .description("Documentación de la API para el sistema de gestión bancaria. Proporciona endpoints para gestionar clientes, cuentas, transacciones y más.")) // Descripción de la API
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
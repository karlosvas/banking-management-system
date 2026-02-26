package com.bytes.ms_accounts.annotations;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.bytes.ms_accounts.dtos.SwaggerErrorDTO;

/**
 * Anotación para definir las respuestas de los endpoints de la API. Manejado por Swagger
 * @Target para indicar que la anotación puede ser aplicada a métodos y clases.
 * @Retention para indicar que la anotación estará disponible en tiempo de ejecución.
 * @ApiResponses para definir las respuestas comunes a los endpoints, con sus respectivos códigos de estado, descripciones y esquemas de respuesta.
 * @interface para definir una anotación, se crea la anotacion SwaggerApiResponses.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "405", description = "Método no permitido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "408", description = "Tiempo de espera agotado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "409", description = "Conflicto", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "422", description = "Entidad no procesable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "502", description = "Puerta de enlace no válida (Error de comunicación entre microservicios)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "503", description = "Servicio no disponible", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class))),
    @ApiResponse(responseCode = "504", description = "Tiempo de espera agotado en la puerta de enlace (El microservicio tardó demasiado en responder)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerErrorDTO.class)))
})
public @interface SwaggerApiResponses {
}

package py.edu.uc.lp3.mm.cs2.rest.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Cuerpo tipado de errores 400, 404, 409 y 500.")
public record ErrorResponse(

        @Schema(description = "Momento del error.", example = "2026-11-25T12:10:44.128")
        LocalDateTime timestamp,

        @Schema(description = "Identificador de traza para correlations.", example = "e8d3f1d0-1234-4c9a-9a2f-0de3e7f3f5b1")
        String traceId,

        @Schema(description = "Código HTTP.", example = "409")
        int status,

        @Schema(description = "Razón del código HTTP.", example = "Conflict")
        String error,

        @Schema(description = "Nombre simple de la excepción.", example = "MunicionInsuficienteException")
        String excepcion,

        @Schema(description = "Mensaje legible del error.", example = "No hay munición suficiente")
        String mensaje,

        @Schema(description = "Ruta solicitada.", example = "/escenarios/disparo-coordinado")
        String path) {
}

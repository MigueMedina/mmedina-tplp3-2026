package py.edu.uc.lp3.mm.cs2.rest.dto;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorValidacionResponse", description = "Cuerpo tipado de errores 400 por validación de entrada.")
public record ErrorValidacionResponse(

        @Schema(description = "Momento del error.")
        LocalDateTime timestamp,

        @Schema(description = "Identificador de traza.")
        String traceId,

        @Schema(description = "Código HTTP.", example = "400")
        int status,

        @Schema(description = "Razón del código HTTP.", example = "Bad Request")
        String error,

        @Schema(description = "Nombre simple de la excepción.", example = "MethodArgumentNotValidException")
        String excepcion,

        @Schema(description = "Mensaje con todos los campos inválidos.")
        String mensaje,

        @Schema(description = "Ruta solicitada.")
        String path,

        @Schema(description = "Primer campo inválido.", example = "jugadorId")
        String campo,

        @Schema(description = "Mapa campo -> mensaje de validación.", example = "{\"jugadorId\":\"debe no ser nulo\"}")
        Map<String, String> errores) {

    public static ErrorValidacionResponse desde(ErrorResponse base, String campo, Map<String, String> errores) {
        return new ErrorValidacionResponse(
                base.timestamp(), base.traceId(), base.status(), base.error(), base.excepcion(),
                base.mensaje(), base.path(), campo, errores);
    }
}

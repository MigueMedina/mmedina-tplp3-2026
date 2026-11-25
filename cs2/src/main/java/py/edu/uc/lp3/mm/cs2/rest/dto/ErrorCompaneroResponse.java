package py.edu.uc.lp3.mm.cs2.rest.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorCompaneroResponse", description = "Cuerpo tipado de rechazos 409 y fallos 502/504 de A2/A3.")
public record ErrorCompaneroResponse(

        @Schema(description = "Momento del error.")
        LocalDateTime timestamp,

        @Schema(description = "Identificador de traza.")
        String traceId,

        @Schema(description = "Código HTTP devuelto por A1.", example = "502")
        int status,

        @Schema(description = "Razón del código HTTP.", example = "Bad Gateway")
        String error,

        @Schema(description = "Nombre simple de la excepción.", example = "CompaneroNoDisponibleException")
        String excepcion,

        @Schema(description = "Mensaje legible del error.")
        String mensaje,

        @Schema(description = "Ruta solicitada.")
        String path,

        @Schema(description = "Compañero que rechazó o no respondió.", example = "A2", allowableValues = {"A2", "A3"})
        String rechazadoPor,

        @Schema(description = "Código HTTP devuelto por el compañero.", example = "503")
        int statusRemoto,

        @Schema(description = "Cuerpo recibido del compañero, recortado.", example = "upstream unavailable")
        String cuerpoRemoto,

        @Schema(description = "Código HTTP expuesto por A1.", example = "502")
        int statusHttp) {

    public static ErrorCompaneroResponse desde(ErrorResponse base, String rechazadoPor,
                                               int statusRemoto, String cuerpoRemoto, int statusHttp) {
        return new ErrorCompaneroResponse(
                base.timestamp(), base.traceId(), base.status(), base.error(), base.excepcion(),
                base.mensaje(), base.path(), rechazadoPor, statusRemoto, cuerpoRemoto, statusHttp);
    }
}

package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de consultar la regla de jugador vivo sin modificar la base.")
public record ReglaJugadorVivoResponse(
        @Schema(description = "Si el jugador puede recibir el disparo.", example = "true") boolean permitido,
        @Schema(description = "Regla aplicada.") String regla,
        @Schema(description = "Detalle de la decisión.") String detalle,
        @Schema(description = "Vida recibida.", example = "100") Integer vida,
        @Schema(description = "Blindaje recibido.", example = "0") Integer blindaje) {
}

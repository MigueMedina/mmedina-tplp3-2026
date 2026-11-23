package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos del escenario coordinado de CS2.")
public record EscenarioRequest(
        @Schema(description = "Id local del jugador objetivo.", example = "1") Long jugadorId,
        @Schema(description = "Id local del arma que dispara.", example = "1") Integer armaId) {
}

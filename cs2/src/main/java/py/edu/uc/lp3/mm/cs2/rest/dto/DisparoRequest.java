package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Identificadores necesarios para simular un disparo.")
public record DisparoRequest(
        @Schema(description = "Id del jugador objetivo.", example = "1") Long jugadorId,
        @Schema(description = "Id del arma que dispara.", example = "1") Integer armaId) {
}

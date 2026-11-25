package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Identificadores necesarios para simular un disparo.")
public record DisparoRequest(
        @NotNull @Schema(description = "Id del jugador objetivo.", example = "1") Long jugadorId,
        @NotNull @Schema(description = "Id del arma que dispara.", example = "1") Integer armaId) {
}

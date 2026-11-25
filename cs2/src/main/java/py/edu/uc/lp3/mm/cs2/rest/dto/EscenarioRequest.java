package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos del escenario coordinado de CS2.")
public record EscenarioRequest(
        @NotNull @Schema(description = "Id local del jugador objetivo.", example = "1") Long jugadorId,
        @NotNull @Schema(description = "Id local del arma que dispara.", example = "1") Integer armaId,
        @Size(max = 40) @Pattern(regexp = "[A-Za-z ]*")
        @Schema(description = "Equipo del tirador; si se omite se usa TERRORISTAS.", example = "TERRORISTAS") String equipoTirador,
        @Size(max = 40) @Pattern(regexp = "[A-Za-z ]*")
        @Schema(description = "Equipo del objetivo; si se omite se usa ANTITERRORISTAS.", example = "ANTITERRORISTAS") String equipoObjetivo,
        @Size(max = 100) @Pattern(regexp = "[A-Za-z0-9._:-]*")
        @Schema(description = "Clave opcional para repetir la misma petición sin duplicar la jugada.",
                example = "escenario-1") String idempotencyKey) {

    public EscenarioRequest(Long jugadorId, Integer armaId) {
        this(jugadorId, armaId, null, null, null);
    }

    public EscenarioRequest(Long jugadorId, Integer armaId, String equipoTirador, String equipoObjetivo) {
        this(jugadorId, armaId, equipoTirador, equipoObjetivo, null);
    }
}

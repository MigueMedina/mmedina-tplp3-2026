package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado persistido de un jugador.")
public record JugadorResponse(
        @Schema(description = "Identificador asignado por H2.", example = "1") Long id,
        @Schema(description = "Nombre del jugador.", example = "Neo") String nombre,
        @Schema(description = "Vida actual, entre 0 y 100.", example = "82") int vida,
        @Schema(description = "Durabilidad restante del chaleco.", example = "82") int blindaje,
        @Schema(description = "false cuando la vida llegó a 0.") boolean vivo) {

    public static JugadorResponse desde(py.edu.uc.lp3.mm.cs2.domain.Jugador jugador) {
        return new JugadorResponse(
                jugador.getId(),
                jugador.getNombre(),
                jugador.getVida(),
                jugador.getBlindaje(),
                jugador.isVivo());
    }
}

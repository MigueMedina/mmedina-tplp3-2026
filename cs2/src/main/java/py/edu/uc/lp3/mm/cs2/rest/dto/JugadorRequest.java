package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para crear o actualizar un jugador. La vida inicial siempre es 100.")
public record JugadorRequest(
        @Schema(description = "Nombre del jugador.", example = "Neo") String nombre,
        @Schema(description = "Durabilidad inicial del chaleco; nulo o 0 significa sin chaleco.", example = "100") Integer blindaje) {

    public JugadorRequest(String nombre) {
        this(nombre, null);
    }
}

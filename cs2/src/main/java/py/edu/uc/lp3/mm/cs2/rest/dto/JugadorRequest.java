package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar un jugador. La vida inicial siempre es 100.")
public record JugadorRequest(
        @NotBlank @Size(max = 120) @Schema(description = "Nombre del jugador.", example = "Neo") String nombre,
        @Min(0) @Max(100) @Schema(description = "Durabilidad inicial del chaleco; nulo o 0 significa sin chaleco.", example = "100") Integer blindaje) {

    public JugadorRequest(String nombre) {
        this(nombre, null);
    }
}

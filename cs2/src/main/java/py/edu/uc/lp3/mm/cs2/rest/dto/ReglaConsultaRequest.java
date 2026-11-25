package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Estado explícito que se consulta, sin ids ni escrituras en la base.")
public record ReglaConsultaRequest(
        @Min(0) @Schema(description = "Balas disponibles en el cargador.", example = "1") Integer carga,
        @Min(1) @Schema(description = "Daño del disparo; debe ser mayor que cero.", example = "30") Integer dano,
        @Min(0) @Max(100) @Schema(description = "Vida actual del jugador, entre 0 y 100.", example = "100") Integer vida,
        @Min(0) @Max(100) @Schema(description = "Blindaje disponible, entre 0 y 100; omitido equivale a 0.", example = "0") Integer blindaje) {
}

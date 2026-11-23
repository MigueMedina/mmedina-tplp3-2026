package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado explícito que se consulta, sin ids ni escrituras en la base.")
public record ReglaConsultaRequest(
        @Schema(description = "Balas disponibles en el cargador.", example = "1") Integer carga,
        @Schema(description = "Daño del disparo; debe ser mayor que cero.", example = "30") Integer dano,
        @Schema(description = "Vida actual del jugador, entre 0 y 100.", example = "100") Integer vida,
        @Schema(description = "Blindaje disponible, entre 0 y 100; omitido equivale a 0.", example = "0") Integer blindaje) {
}

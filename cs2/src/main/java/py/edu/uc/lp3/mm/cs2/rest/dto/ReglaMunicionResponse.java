package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de consultar la regla de munición sin modificar la base.")
public record ReglaMunicionResponse(
        @Schema(description = "Si el arma tiene munición para disparar.", example = "true") boolean permitido,
        @Schema(description = "Regla aplicada.") String regla,
        @Schema(description = "Detalle de la decisión.") String detalle,
        @Schema(description = "Carga recibida.", example = "1") Integer carga,
        @Schema(description = "Daño recibido.", example = "30") Integer dano) {
}

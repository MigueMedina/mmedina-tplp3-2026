package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de entrada para crear y actualizar armas. Los campos nulos se completan
 * con valores por defecto al crear y se ignoran al actualizar.
 */
@Schema(description = "Datos para crear o actualizar un arma de la jerarquía CS2.")
public record ArmaRequest(
        @Schema(description = "Subtipo de arma: fusil, pistola, escopeta, awp, flash, humo o granada.", example = "fusil") String tipo,
        @Schema(description = "Nombre comercial del arma.", example = "AK-47") String nombre,
        @Schema(description = "Precio en dinero del juego.", example = "2700") Double precio,
        @Schema(description = "Daño base del arma.", example = "36") Integer dano,
        @Schema(description = "Precisión del arma.", example = "75") Integer precision,
        @Schema(description = "Tiempo de recarga en segundos.", example = "2.5") Double recarga,
        @Schema(description = "Velocidad de disparo.", example = "10") Double velocidad,
        @Schema(description = "Capacidad del cargador.", example = "30") Integer capacidadCargador,
        @Schema(description = "Indica si el arma es automática.", example = "true") Boolean automatica,
        @Schema(description = "Indica si el arma tiene silenciador.", example = "false") Boolean silenciador,
        @Schema(description = "Nivel de mira.", example = "2") Integer mira,
        @Schema(description = "Retroceso del arma.", example = "20") Integer retroceso,
        @Schema(description = "Radio de efecto para arrojadizas.", example = "5") Double radio,
        @Schema(description = "Distancia de efecto para arrojadizas.", example = "20") Double distancia,
        @Schema(description = "Duración del efecto.", example = "15") Double duracion,
        @Schema(description = "Tiempo de enfriamiento.", example = "0") Double cooldown,
        @Schema(description = "Intensidad del efecto para flash.", example = "50") Integer intensidad,
        @Schema(description = "Duración de la ceguera.", example = "1.5") Double duracionCeguera,
        @Schema(description = "Indica si la granada aturde.", example = "false") Boolean aturdimiento,
        @Schema(description = "Cantidad de perdigones.", example = "8") Integer perdigon,
        @Schema(description = "Dispersión de los perdigones.", example = "2.5") Double dispersion) {
}

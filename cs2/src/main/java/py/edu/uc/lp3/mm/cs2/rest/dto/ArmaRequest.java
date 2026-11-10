package py.edu.uc.lp3.mm.cs2.rest.dto;

/**
 * DTO de entrada para crear y actualizar armas. Los campos nulos se completan
 * con valores por defecto al crear y se ignoran al actualizar.
 */
public record ArmaRequest(
        String tipo,
        String nombre,
        Double precio,
        Integer dano,
        Integer precision,
        Double recarga,
        Double velocidad,
        Integer capacidadCargador,
        Boolean automatica,
        Boolean silenciador,
        Integer mira,
        Integer retroceso,
        Double radio,
        Double distancia,
        Double duracion,
        Double cooldown,
        Integer intensidad,
        Double duracionCeguera,
        Boolean aturdimiento,
        Integer perdigon,
        Double dispersion) {
}

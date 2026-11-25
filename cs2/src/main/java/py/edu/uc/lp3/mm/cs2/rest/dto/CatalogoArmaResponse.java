package py.edu.uc.lp3.mm.cs2.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.uc.lp3.mm.cs2.constants.CatalogoArma;

@Schema(name = "CatalogoArmaResponse", description = "Datos orientativos de un arma del catálogo CS2 de A1.")
public record CatalogoArmaResponse(

        @Schema(description = "Identificador estable del modelo.", example = "AK47")
        String modelo,

        @Schema(description = "Nombre comercial.", example = "AK-47")
        String nombre,

        @Schema(description = "Familia del arma.", example = "fusil")
        String familia,

        @Schema(description = "Equipo que puede comprarla.", example = "TT", allowableValues = {"CT", "TT", "AMBOS"})
        String equipo,

        @Schema(description = "Precio en dólares del juego.", example = "2700")
        int precio,

        @Schema(description = "Daño base por bala.", example = "38")
        int dano,

        @Schema(description = "Precisión base.", example = "74")
        int precision,

        @Schema(description = "Balas en el cargador.", example = "30")
        int capacidadCargador,

        @Schema(description = "Balas de reserva.", example = "90")
        int reservaBalas,

        @Schema(description = "Balas consumidas por ráfaga.", example = "3")
        int rafagaBalas,

        @Schema(description = "Alcance útil en metros.", example = "130")
        int alcanceMetros,

        @Schema(description = "Relación de penetración de blindaje.", example = "0.78")
        double relacionBlindaje,

        @Schema(description = "Uso recomendado.", example = "Fusil automático potente.")
        String descripcion) {

    public static CatalogoArmaResponse desde(CatalogoArma arma) {
        return new CatalogoArmaResponse(
                arma.name(),
                arma.getNombre(),
                arma.getFamilia(),
                arma.getEquipo(),
                arma.getPrecio(),
                arma.getDano(),
                arma.getPrecision(),
                arma.getCapacidadCargador(),
                arma.getReservaBalas(),
                arma.getRafagaBalas(),
                arma.getAlcanceMetros(),
                arma.getRelacionBlindaje(),
                arma.getDescripcion());
    }
}

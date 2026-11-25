package py.edu.uc.lp3.mm.cs2.rest.dto;

import java.time.LocalDateTime;
import java.util.Locale;

import org.hibernate.Hibernate;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.uc.lp3.mm.cs2.domain.Simulacion;

@Schema(description = "Resultado persistido de una simulación de disparo.")
public record SimulacionResponse(
        @Schema(description = "Id del historial.", example = "1") Long id,
        @Schema(description = "Id del jugador objetivo.", example = "1") Long jugadorId,
        @Schema(description = "Nombre del jugador.", example = "Neo") String jugador,
        @Schema(description = "Vida antes del disparo.", example = "100") int vidaAntes,
        @Schema(description = "Vida después del disparo.", example = "82") int vidaDespues,
        @Schema(description = "Daño efectivo descontado de la vida.", example = "18") int danoAplicado,
        @Schema(description = "false si el jugador quedó eliminado.") boolean vivo,
        @Schema(description = "Id del arma usada.", example = "1") Integer armaId,
        @Schema(description = "Nombre del arma.", example = "AK-47") String arma,
        @Schema(description = "Subtipo del arma en minúsculas.", example = "fusil") String tipoArma,
        @Schema(description = "Daño base del arma.", example = "36") int dano,
        @Schema(description = "Fecha y hora del disparo.") LocalDateTime fecha) {

    public static SimulacionResponse desde(Simulacion simulacion) {
        return new SimulacionResponse(
                simulacion.getId(),
                simulacion.getJugador().getId(),
                simulacion.getJugador().getNombre(),
                simulacion.getVidaAntes(),
                simulacion.getVidaDespues(),
                simulacion.getVidaAntes() - simulacion.getVidaDespues(),
                simulacion.getJugador().isVivo(),
                simulacion.getArma().getId(),
                simulacion.getArma().getNombre(),
                Hibernate.getClass(simulacion.getArma()).getSimpleName().toLowerCase(Locale.ROOT),
                simulacion.getDano(),
                simulacion.getFecha());
    }
}

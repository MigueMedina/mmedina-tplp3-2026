package py.edu.uc.lp3.mm.cs2.rest.dto;

import java.time.LocalDateTime;
import java.util.Locale;

import py.edu.uc.lp3.mm.cs2.domain.Simulacion;

public record SimulacionResponse(
        Long id,
        Long jugadorId,
        String jugador,
        int vidaAntes,
        int vidaDespues,
        int danoAplicado,
        boolean vivo,
        Integer armaId,
        String arma,
        String tipoArma,
        int dano,
        LocalDateTime fecha) {

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
                simulacion.getArma().getClass().getSimpleName().toLowerCase(Locale.ROOT),
                simulacion.getDano(),
                simulacion.getFecha());
    }
}

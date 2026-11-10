package py.edu.uc.lp3.mm.cs2.rest.dto;

import py.edu.uc.lp3.mm.cs2.domain.Jugador;

public record JugadorResponse(Long id, String nombre, int vida, int blindaje, boolean vivo) {

    public static JugadorResponse desde(Jugador jugador) {
        return new JugadorResponse(
                jugador.getId(),
                jugador.getNombre(),
                jugador.getVida(),
                jugador.getBlindaje(),
                jugador.isVivo());
    }
}

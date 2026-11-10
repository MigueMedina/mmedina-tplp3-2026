package py.edu.uc.lp3.mm.cs2.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.JugadorEliminadoException;

class JugadorDomainTest {

    @Test
    void iniciaConCienDeVida() {
        Jugador jugador = new Jugador("Neo");

        assertEquals(100, jugador.getVida());
        assertTrue(jugador.isVivo());
    }

    @Test
    void recibirDisparoReduceLaVida() {
        Jugador jugador = new Jugador("Neo");

        int danoAplicado = jugador.recibirDisparo(35);

        assertEquals(35, danoAplicado);
        assertEquals(65, jugador.getVida());
        assertTrue(jugador.isVivo());
    }

    @Test
    void laVidaNoBajaDeCeroYElJugadorMuere() {
        Jugador jugador = new Jugador("Neo");

        int danoAplicado = jugador.recibirDisparo(250);

        assertEquals(100, danoAplicado);
        assertEquals(0, jugador.getVida());
        assertFalse(jugador.isVivo());
    }

    @Test
    void noSePuedeDispararAUnJugadorMuerto() {
        Jugador jugador = new Jugador("Neo");
        jugador.recibirDisparo(100);

        assertThrows(JugadorEliminadoException.class, () -> jugador.recibirDisparo(10));
    }

    @Test
    void unDanoNoPositivoEsInvalido() {
        Jugador jugador = new Jugador("Neo");

        assertThrows(DatosInvalidosException.class, () -> jugador.recibirDisparo(0));
    }

    @Test
    void elChalecoAbsorbeLaMitadYSeDesgasta() {
        Jugador jugador = new Jugador("Neo", 100, new Chaleco(100));

        int danoAplicado = jugador.recibirDisparo(36);

        assertEquals(18, danoAplicado);
        assertEquals(82, jugador.getVida());
        assertEquals(82, jugador.getBlindaje());
    }

    @Test
    void elChalecoNoAbsorbeMasQueSuDurabilidadRestante() {
        Jugador jugador = new Jugador("Neo", 100, new Chaleco(10));

        int danoAplicado = jugador.recibirDisparo(40);

        assertEquals(30, danoAplicado);
        assertEquals(70, jugador.getVida());
        assertEquals(0, jugador.getBlindaje());
    }

    @Test
    void unChalecoConDurabilidadInvalidaEsRechazado() {
        assertThrows(DatosInvalidosException.class, () -> new Chaleco(-1));
        assertThrows(DatosInvalidosException.class, () -> new Chaleco(101));
    }
}

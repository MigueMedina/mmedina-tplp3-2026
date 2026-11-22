package py.edu.uc.lp3.mm.cs2.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.mm.cs2.domain.AWP;
import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.Chaleco;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;

class ValidadorServiceTest {

    private final ValidadorService validador = new ValidadorService();

    @Test
    void nombreDeArmaVacioEsInvalido() {
        Arma arma = new Fusil("", 1, 100, 10, 50, 1, 1, 30, true, 1, 1, false);

        assertThrows(DatosInvalidosException.class, () -> validador.validarArma(arma));
    }

    @Test
    void precioNegativoEsInvalido() {
        Arma arma = new Fusil("AK", 1, -100, 10, 50, 1, 1, 30, true, 1, 1, false);

        assertThrows(DatosInvalidosException.class, () -> validador.validarArma(arma));
    }

    @Test
    void idNegativoEsInvalido() {
        Arma arma = new AWP("AWP", -1, 4750, 115, 98, 3, 1, 10, 1, 1, false);

        assertThrows(DatosInvalidosException.class, () -> validador.validarArma(arma));
    }

    @Test
    void precisionFueraDeRangoEsInvalida() {
        Arma arma = new AWP("AWP", 1, 4750, 115, 150, 3, 1, 10, 1, 1, false);

        assertThrows(DatosInvalidosException.class, () -> validador.validarArma(arma));
    }

    @Test
    void capacidadDeCargadorNegativaEsInvalida() {
        Arma arma = new Fusil("AK", 1, 2700, 36, 75, 2, 10, -30, true, 1, 1, false);

        assertThrows(DatosInvalidosException.class, () -> validador.validarArma(arma));
    }

    @Test
    void radioNegativoDeArrojadizaEsInvalido() {
        Arma arma = new Granada("HE", 1, 300, "granada", -6, 0, 0, 0, 98, true);

        assertThrows(DatosInvalidosException.class, () -> validador.validarArma(arma));
    }

    @Test
    void nombreYVidaDeJugadorInvalidos() {
        assertThrows(DatosInvalidosException.class,
                () -> validador.validarJugador(new Jugador("")));
        assertThrows(DatosInvalidosException.class,
                () -> validador.validarJugador(new Jugador("Neo", -1)));
        assertThrows(DatosInvalidosException.class,
                () -> validador.validarJugador(new Jugador("Neo", 101)));
    }

    @Test
    void durabilidadDeChalecoInvalida() {
        assertThrows(DatosInvalidosException.class, () -> validador.validarChaleco(-1));
        assertThrows(DatosInvalidosException.class,
                () -> validador.validarChaleco(Chaleco.DURABILIDAD_MAXIMA + 1));
    }

    @Test
    void danoNoPositivoEsInvalido() {
        assertThrows(DatosInvalidosException.class, () -> validador.validarDano(0));
        assertThrows(DatosInvalidosException.class, () -> validador.validarDano(-1));
        assertDoesNotThrow(() -> validador.validarDano(36));
    }

    @Test
    void recargarSinRepuestosLanzaLaExcepcionDelServicio() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 30, true, 1, 1, false);
        for (int disparo = 0; disparo < 30; disparo++) {
            fusil.disparar();
        }
        fusil.setCargadoresRestantes(0);

        assertThrows(MunicionInsuficienteException.class, () -> validador.validarRecarga(fusil));
    }

    @Test
    void datosValidosNoLanzanExcepcion() {
        assertDoesNotThrow(() -> validador.validarArma(
                new Fusil("AK-47", 1, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false)));
        assertDoesNotThrow(() -> validador.validarJugador(new Jugador("Neo")));
        assertDoesNotThrow(() -> validador.validarChaleco(Chaleco.DURABILIDAD_MAXIMA));
    }
}

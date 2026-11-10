package py.edu.uc.lp3.mm.cs2.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;

class ArmaDomainTest {

    @Test
    void nombreVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Fusil("", 1, 100, 10, 50, 1, 1, 30, true, 1, 1, false));
    }

    @Test
    void precioNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pistola("USP", 1, -100, 10, 50, 1, 1, 12, false, false));
    }

    @Test
    void idNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new AWP("AWP", -1, 4750, 115, 98, 3, 1, 10, 1, 1, false));
    }

    @Test
    void precisionFueraDeRangoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new AWP("AWP", 1, 4750, 115, 150, 3, 1, 10, 1, 1, false));
    }

    @Test
    void capacidadNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Fusil("AK", 1, 2700, 36, 75, 2, 10, -30, true, 1, 1, false));
    }

    @Test
    void municionFueraDeCapacidadLanzaExcepcion() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 30, true, 1, 1, false);
        assertThrows(IllegalArgumentException.class, () -> fusil.setMunicionActual(31));
        assertThrows(IllegalArgumentException.class, () -> fusil.setMunicionActual(-1));
    }

    @Test
    void valoresNegativosEnArrojadizaLanzanExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Granada("HE", 1, 300, "granada", -6, 0, 0, 0, 98, true));
    }

    @Test
    void polimorfismoSobreListaDeArmas() {
        List<Arma> armas = List.of(
                new Fusil("AK-47", 1, 2700, 36, 75, 2.5, 10, 30, true, 1, 20, false),
                new Pistola("USP", 2, 200, 35, 80, 2.2, 8, 12, false, true),
                new Granada("HE", 3, 300, "granada", 6, 0, 0, 0, 98, true),
                new Humo("Smoke", 4, 300, "humo", 5, 0, 15, 0));

        for (Arma arma : armas) {
            assertFalse(arma.disparar().isBlank());
            assertFalse(arma.obtenerDetalleTienda().isBlank());
        }
        assertEquals(4, armas.stream().map(Arma::disparar).distinct().count());
    }

    @Test
    void polimorfismoUICapable() {
        List<UICapable> elementos = List.of(
                new Fusil("AK-47", 1, 2700, 36, 75, 2.5, 10, 30, true, 1, 20, false),
                new AWP("AWP", 2, 4750, 115, 98, 3.7, 5, 10, 2, 30, false),
                new Flash("Flashbang", 3, 200, "flash", 5, 20, 2, 0.5, 90, 3.5),
                new Granada("HE", 4, 300, "granada", 6, 30, 2, 1, 98, true));

        for (UICapable elemento : elementos) {
            assertFalse(elemento.getImagen().isBlank());
            assertFalse(elemento.getSonido().isBlank());
            assertTrue(elemento.getRango() >= 0);
        }
    }

    @Test
    void sobrecargaRecargarParcialConsumeCargadorDeRepuesto() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 30, true, 1, 1, false);
        fusil.setMunicionActual(0);

        assertTrue(fusil.recargar(10).contains("Cargadores restantes: 2"));
        assertEquals(10, fusil.getMunicionActual());
        assertEquals(2, fusil.getCargadoresRestantes());

        fusil.recargar(100);
        assertEquals(30, fusil.getMunicionActual());
        assertEquals(1, fusil.getCargadoresRestantes());

        assertThrows(IllegalArgumentException.class, () -> fusil.recargar(0));
    }

    @Test
    void laRecargaParcialFallaSinCargadoresDeRepuesto() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 30, true, 1, 1, false);
        fusil.setMunicionActual(0);
        fusil.setCargadoresRestantes(0);

        assertThrows(MunicionInsuficienteException.class, () -> fusil.recargar(10));
        assertEquals(0, fusil.getMunicionActual());
        assertEquals(0, fusil.getCargadoresRestantes());
    }

    @Test
    void dispararDescuentaMunicionYFallaConElCargadorVacio() {
        Pistola pistola = new Pistola("USP", 1, 200, 35, 80, 2.2, 8, 2, false, true);
        pistola.setMunicionActual(2);

        assertTrue(pistola.disparar().contains("1/2"));
        assertEquals(1, pistola.getMunicionActual());
        assertTrue(pistola.disparar().contains("0/2"));
        assertEquals(0, pistola.getMunicionActual());

        assertThrows(MunicionInsuficienteException.class, pistola::disparar);
    }

    @Test
    void recargarConsumeCargadoresDeRepuestoYFallaSinEllos() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 2, true, 1, 1, false);
        fusil.setMunicionActual(0);
        fusil.setCargadoresRestantes(1);

        assertTrue(fusil.recargar().contains("Cargadores restantes: 0"));
        assertEquals(2, fusil.getMunicionActual());
        assertEquals(0, fusil.getCargadoresRestantes());

        fusil.setMunicionActual(0);
        assertThrows(MunicionInsuficienteException.class, fusil::recargar);
    }
}

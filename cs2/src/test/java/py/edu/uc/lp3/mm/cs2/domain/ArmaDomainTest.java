package py.edu.uc.lp3.mm.cs2.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class ArmaDomainTest {

    @Test
    void creaUnFusilConEstadoInicial() {
        Fusil fusil = new Fusil("AK-47", 1, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false);

        assertEquals("AK-47", fusil.getNombre());
        assertEquals(2700, fusil.getPrecio());
        assertEquals(36, fusil.getDano());
        assertEquals(30, fusil.getMunicionActual());
        assertEquals(3, fusil.getCargadoresRestantes());
        assertTrue(fusil.isAutomatica());
        assertFalse(fusil.isSilenciador());
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
    void polimorfismoUICapableIncluyeAJugador() {
        List<UICapable> elementos = List.of(
                new Fusil("AK-47", 1, 2700, 36, 75, 2.5, 10, 30, true, 1, 20, false),
                new AWP("AWP", 2, 4750, 115, 98, 3.7, 5, 10, 2, 30, false),
                new Flash("Flashbang", 3, 200, "flash", 5, 20, 2, 0.5, 90, 3.5),
                new Granada("HE", 4, 300, "granada", 6, 30, 2, 1, 98, true),
                new Jugador("Neo"));

        for (UICapable elemento : elementos) {
            assertFalse(elemento.getImagen().isBlank());
            assertFalse(elemento.getSonido().isBlank());
            assertTrue(elemento.getRango() >= 0);
            assertFalse(elemento.mostrarEnUI().isBlank());
        }
    }

    @Test
    void sobrecargaRecargarParcialConsumeCargadorDeRepuesto() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 30, true, 1, 1, false);
        for (int disparo = 0; disparo < 30; disparo++) {
            fusil.disparar();
        }
        assertEquals(0, fusil.getMunicionActual());

        assertTrue(fusil.recargar(10).contains("Cargadores restantes: 2"));
        assertEquals(10, fusil.getMunicionActual());
        assertEquals(2, fusil.getCargadoresRestantes());

        fusil.recargar(100);
        assertEquals(30, fusil.getMunicionActual());
        assertEquals(1, fusil.getCargadoresRestantes());

        assertTrue(fusil.recargar().contains("ya tiene el cargador lleno"));
        assertEquals(30, fusil.getMunicionActual());
        assertEquals(1, fusil.getCargadoresRestantes());
    }

    @Test
    void laRecargaParcialSinRepuestosNoModificaElEstado() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 30, true, 1, 1, false);
        for (int disparo = 0; disparo < 30; disparo++) {
            fusil.disparar();
        }
        fusil.setCargadoresRestantes(0);

        assertTrue(fusil.recargar(10).contains("no tiene cargadores de repuesto"));
        assertEquals(0, fusil.getMunicionActual());
        assertEquals(0, fusil.getCargadoresRestantes());
    }

    @Test
    void dispararDescuentaMunicionHastaAgotarse() {
        Pistola pistola = new Pistola("USP", 1, 200, 35, 80, 2.2, 8, 2, false, true);

        assertTrue(pistola.disparar().contains("1/2"));
        assertEquals(1, pistola.getMunicionActual());
        assertTrue(pistola.disparar().contains("0/2"));
        assertEquals(0, pistola.getMunicionActual());

        pistola.disparar();
        assertEquals(0, pistola.getMunicionActual());
    }

    @Test
    void recargarConsumeCargadoresDeRepuesto() {
        Fusil fusil = new Fusil("AK", 1, 2700, 36, 75, 2, 10, 2, true, 1, 1, false);
        for (int disparo = 0; disparo < 2; disparo++) {
            fusil.disparar();
        }
        fusil.setCargadoresRestantes(1);

        assertTrue(fusil.recargar().contains("Cargadores restantes: 0"));
        assertEquals(2, fusil.getMunicionActual());
        assertEquals(0, fusil.getCargadoresRestantes());
    }
}

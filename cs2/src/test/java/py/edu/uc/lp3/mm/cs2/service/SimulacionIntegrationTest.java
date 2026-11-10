package py.edu.uc.lp3.mm.cs2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.repository.FusilRepository;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;
import py.edu.uc.lp3.mm.cs2.repository.SimulacionRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.DisparoRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.JugadorRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.SimulacionResponse;

@SpringBootTest
@Transactional
class SimulacionIntegrationTest {

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private SimulacionService simulacionService;

    @Autowired
    private FusilRepository fusilRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private SimulacionRepository simulacionRepository;

    @Test
    void elDisparoReduceYPersisteLaVidaDelJugador() {
        Jugador jugador = jugadorService.crear(new JugadorRequest("Neo"));
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));

        SimulacionResponse respuesta = simulacionService.disparar(
                new DisparoRequest(jugador.getId(), fusil.getId()));

        assertEquals(100, respuesta.vidaAntes());
        assertEquals(64, respuesta.vidaDespues());
        assertEquals(36, respuesta.dano());
        assertEquals("fusil", respuesta.tipoArma());
        assertTrue(respuesta.vivo());
        assertEquals(1, simulacionRepository.count());

        Jugador persistido = jugadorRepository.findById(jugador.getId()).orElseThrow();
        assertEquals(64, persistido.getVida());

        Fusil fusilPersistido = fusilRepository.findById(fusil.getId()).orElseThrow();
        assertEquals(29, fusilPersistido.getMunicionActual());
    }

    @Test
    void elChalecoAbsorbeDanoYSuDurabilidadQuedaPersistida() {
        Jugador jugador = jugadorService.crear(new JugadorRequest("Neo", 100));
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));

        SimulacionResponse respuesta = simulacionService.disparar(
                new DisparoRequest(jugador.getId(), fusil.getId()));

        assertEquals(100, respuesta.vidaAntes());
        assertEquals(82, respuesta.vidaDespues());
        assertEquals(18, respuesta.danoAplicado());
        assertEquals(36, respuesta.dano());

        Jugador persistido = jugadorRepository.findById(jugador.getId()).orElseThrow();
        assertEquals(82, persistido.getVida());
        assertEquals(82, persistido.getBlindaje());
    }

    @Test
    void unSegundoDisparoMortalDejaAlJugadorSinVida() {
        Jugador jugador = jugadorService.crear(new JugadorRequest("Trinity"));
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 60, 75, 2.5, 10, 30, true, 2, 20, false));

        simulacionService.disparar(new DisparoRequest(jugador.getId(), fusil.getId()));
        SimulacionResponse segundo = simulacionService.disparar(
                new DisparoRequest(jugador.getId(), fusil.getId()));

        assertEquals(40, segundo.vidaAntes());
        assertEquals(0, segundo.vidaDespues());
        assertFalse(segundo.vivo());
        assertEquals(2, simulacionRepository.count());
    }
}

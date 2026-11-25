package py.edu.uc.lp3.mm.cs2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.client.CompaneroClient;
import py.edu.uc.lp3.mm.cs2.client.ReglaRequest;
import py.edu.uc.lp3.mm.cs2.client.RespuestaCompanero;
import py.edu.uc.lp3.mm.cs2.client.RolCompanero;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.exception.CompanneroRechazoException;
import py.edu.uc.lp3.mm.cs2.repository.EscenarioRepository;
import py.edu.uc.lp3.mm.cs2.repository.FusilRepository;
import py.edu.uc.lp3.mm.cs2.repository.SimulacionRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.JugadorRequest;

@SpringBootTest
@Transactional
class EscenarioIntegrationTest {

    @MockitoBean
    private CompaneroClient companeroClient;

    @Autowired
    private EscenarioService escenarioService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private FusilRepository fusilRepository;

    @Autowired
    private EscenarioRepository escenarioRepository;

    @Autowired
    private SimulacionRepository simulacionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void guardaLaJugadaSoloCuandoA2YA3Aceptan() {
        Long jugadorId = jugadorService.crear(new JugadorRequest("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        aprobaciones();

        EscenarioResponse respuesta = escenarioService.ejecutar(
                new EscenarioRequest(jugadorId, fusil.getId()));

        assertEquals(1, escenarioRepository.count());
        assertEquals(1, simulacionRepository.count());
        assertEquals(64, respuesta.simulacion().vidaDespues());
        assertEquals("A2", respuesta.a2().companero());
        assertEquals("A3", respuesta.a3().companero());
        assertEquals(200, respuesta.a2().status());
        assertEquals(200, respuesta.a3().status());

        InOrder orden = inOrder(companeroClient);
        orden.verify(companeroClient).validar(eq(RolCompanero.A2), any(ReglaRequest.class));
        orden.verify(companeroClient).validar(eq(RolCompanero.A3), any(ReglaRequest.class));

        ArgumentCaptor<ReglaRequest> cuerpoA2 = ArgumentCaptor.forClass(ReglaRequest.class);
        ArgumentCaptor<ReglaRequest> cuerpoA3 = ArgumentCaptor.forClass(ReglaRequest.class);
        verify(companeroClient).validar(eq(RolCompanero.A2), cuerpoA2.capture());
        verify(companeroClient).validar(eq(RolCompanero.A3), cuerpoA3.capture());
        assertEquals(30, cuerpoA2.getValue().valores().get("cargador"));
        assertEquals(1, cuerpoA2.getValue().valores().get("balasPorDisparo"));
        assertEquals(1, cuerpoA2.getValue().valores().get("disparos"));
        assertEquals("A1-MigueMedina", cuerpoA3.getValue().valores().get("tirador"));
        assertEquals("TERRORISTAS", cuerpoA3.getValue().valores().get("equipoTirador"));
        assertEquals("Neo", cuerpoA3.getValue().valores().get("objetivo"));
        assertEquals("ANTITERRORISTAS", cuerpoA3.getValue().valores().get("equipoObjetivo"));
        assertEquals(100, cuerpoA3.getValue().valores().get("vidaObjetivo"));
        assertEquals(0, cuerpoA3.getValue().valores().get("blindajeObjetivo"));
        assertEquals(36, cuerpoA3.getValue().valores().get("danio"));

        assertEquals(29, fusilRepository.findById(fusil.getId()).orElseThrow().getMunicionActual());
    }

    @Test
    void elRechazoDeA2NoEscribeYNoConsultaAA3() {
        Long jugadorId = jugadorService.crear(new JugadorRequest("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        when(companeroClient.validar(eq(RolCompanero.A2), any(ReglaRequest.class)))
                .thenThrow(new CompanneroRechazoException(
                        RolCompanero.A2, 409, "{\"excepcion\":\"MunicionInsuficienteException\"}"));

        CompanneroRechazoException excepcion = assertThrows(CompanneroRechazoException.class,
                () -> escenarioService.ejecutar(new EscenarioRequest(jugadorId, fusil.getId())));

        assertEquals(RolCompanero.A2, excepcion.getRechazadoPor());
        assertEquals(0, escenarioRepository.count());
        assertEquals(0, simulacionRepository.count());
        assertEquals(100, jugadorService.buscar(jugadorId).getVida());
        assertEquals(30, fusilRepository.findById(fusil.getId()).orElseThrow().getMunicionActual());
        verify(companeroClient, never()).validar(eq(RolCompanero.A3), any(ReglaRequest.class));
    }

    @Test
    void elRechazoDeA3NoEscribeAunqueA2Acepte() {
        Long jugadorId = jugadorService.crear(new JugadorRequest("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        when(companeroClient.validar(eq(RolCompanero.A2), any(ReglaRequest.class)))
                .thenReturn(new RespuestaCompanero(RolCompanero.A2, 200, "{\"permitido\":true}", "POST /r/a2"));
        when(companeroClient.validar(eq(RolCompanero.A3), any(ReglaRequest.class)))
                .thenThrow(new CompanneroRechazoException(
                        RolCompanero.A3, 409, "{\"excepcion\":\"JugadorEliminadoException\"}"));

        CompanneroRechazoException excepcion = assertThrows(CompanneroRechazoException.class,
                () -> escenarioService.ejecutar(new EscenarioRequest(jugadorId, fusil.getId())));

        assertEquals(RolCompanero.A3, excepcion.getRechazadoPor());
        assertEquals(0, escenarioRepository.count());
        assertEquals(0, simulacionRepository.count());
        assertEquals(100, jugadorService.buscar(jugadorId).getVida());
        assertEquals(30, fusilRepository.findById(fusil.getId()).orElseThrow().getMunicionActual());
    }

    @Test
    void noPersistaCuandoElCuerpoDeA2DiceQueNoHuboAprobacion() {
        Long jugadorId = jugadorService.crear(new JugadorRequest("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        when(companeroClient.validar(eq(RolCompanero.A2), any(ReglaRequest.class)))
                .thenReturn(new RespuestaCompanero(RolCompanero.A2, 200, "{\"permitido\":false}", "POST /r/a2"));

        CompanneroRechazoException excepcion = assertThrows(CompanneroRechazoException.class,
                () -> escenarioService.ejecutar(new EscenarioRequest(jugadorId, fusil.getId())));

        assertEquals(RolCompanero.A2, excepcion.getRechazadoPor());
        assertEquals(0, escenarioRepository.count());
        assertEquals(0, simulacionRepository.count());
        verify(companeroClient, never()).validar(eq(RolCompanero.A3), any(ReglaRequest.class));
    }

    @Test
    void laClaveDeIdempotenciaEvitaRepetirLaJugada() {
        Long jugadorId = jugadorService.crear(new JugadorRequest("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        aprobaciones();
        EscenarioRequest request = new EscenarioRequest(jugadorId, fusil.getId(), null, null, "escenario-unico");

        EscenarioResponse primera = escenarioService.ejecutar(request);
        EscenarioResponse segunda = escenarioService.ejecutar(request);

        assertEquals(primera.id(), segunda.id());
        assertEquals(1, escenarioRepository.count());
        assertEquals(1, simulacionRepository.count());
        assertEquals(29, fusilRepository.findById(fusil.getId()).orElseThrow().getMunicionActual());
        verify(companeroClient, times(1)).validar(eq(RolCompanero.A2), any(ReglaRequest.class));
        verify(companeroClient, times(1)).validar(eq(RolCompanero.A3), any(ReglaRequest.class));
    }

    @Test
    void laSimulacionDeUnEscenarioNoSePuedeBorrarSinElEscenario() {
        Long jugadorId = jugadorService.crear(new JugadorRequest("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        aprobaciones();
        EscenarioResponse respuesta = escenarioService.ejecutar(
                new EscenarioRequest(jugadorId, fusil.getId()));

        assertThrows(DataIntegrityViolationException.class,
                () -> jdbcTemplate.update("delete from simulaciones where id = ?", respuesta.simulacion().id()));
    }

    private void aprobaciones() {
        when(companeroClient.validar(eq(RolCompanero.A2), any(ReglaRequest.class)))
                .thenReturn(new RespuestaCompanero(RolCompanero.A2, 200, "{\"permitido\":true}", "POST /r/a2"));
        when(companeroClient.validar(eq(RolCompanero.A3), any(ReglaRequest.class)))
                .thenReturn(new RespuestaCompanero(RolCompanero.A3, 200, "{\"permitido\":true}", "POST /r/a3"));
    }
}

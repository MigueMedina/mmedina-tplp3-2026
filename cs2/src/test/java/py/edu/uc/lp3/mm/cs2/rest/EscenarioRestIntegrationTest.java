package py.edu.uc.lp3.mm.cs2.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.client.CompaneroClient;
import py.edu.uc.lp3.mm.cs2.client.ReglaRequest;
import py.edu.uc.lp3.mm.cs2.client.RespuestaCompanero;
import py.edu.uc.lp3.mm.cs2.client.RolCompanero;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.exception.CompanneroRechazoException;
import py.edu.uc.lp3.mm.cs2.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.mm.cs2.repository.FusilRepository;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;
import py.edu.uc.lp3.mm.cs2.repository.SimulacionRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EscenarioRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private FusilRepository fusilRepository;

    @Autowired
    private SimulacionRepository simulacionRepository;

    @MockitoBean
    private CompaneroClient companeroClient;

    @Test
    void elEscenarioFelizSeGuardaYSeVuelveALeer() throws Exception {
        Long jugadorId = jugadorRepository.save(new Jugador("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        aprobaciones();

        String contenido = mockMvc.perform(post("/escenarios/disparo-coordinado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jugadorId":%d,"armaId":%d}
                                """.formatted(jugadorId, fusil.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.a2.companero").value("A2"))
                .andExpect(jsonPath("$.a3.companero").value("A3"))
                .andExpect(jsonPath("$.simulacion.vidaDespues").value(64))
                .andReturn().getResponse().getContentAsString();

        Long escenarioId = extraerId(contenido);
        mockMvc.perform(get("/escenarios/" + escenarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simulacion.jugador").value("Neo"));
        mockMvc.perform(get("/escenarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].a2.status").value(200));
    }

    @Test
    void elRechazoDeA3SeAtribuyeYNoModificaLaBase() throws Exception {
        Long jugadorId = jugadorRepository.save(new Jugador("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        when(companeroClient.validar(eq(RolCompanero.A2), any(ReglaRequest.class)))
                .thenReturn(new RespuestaCompanero(RolCompanero.A2, 200, "{\"permitido\":true}", "POST /r/a2"));
        when(companeroClient.validar(eq(RolCompanero.A3), any(ReglaRequest.class)))
                .thenThrow(new CompanneroRechazoException(
                        RolCompanero.A3, 409, "{\"excepcion\":\"JugadorEliminadoException\"}"));

        mockMvc.perform(post("/escenarios/disparo-coordinado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jugadorId":%d,"armaId":%d}
                                """.formatted(jugadorId, fusil.getId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.excepcion").value("CompanneroRechazoException"))
                .andExpect(jsonPath("$.rechazadoPor").value("A3"))
                .andExpect(jsonPath("$.statusRemoto").value(409))
                .andExpect(jsonPath("$.statusHttp").value(409))
                .andExpect(jsonPath("$.cuerpoRemoto").value("{\"excepcion\":\"JugadorEliminadoException\"}"))
                .andExpect(jsonPath("$.traceId").exists());

        mockMvc.perform(get("/escenarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        org.junit.jupiter.api.Assertions.assertEquals(0, simulacionRepository.count());
        org.junit.jupiter.api.Assertions.assertEquals(30,
                fusilRepository.findById(fusil.getId()).orElseThrow().getMunicionActual());
    }

    @Test
    void validaElCuerpoAntesDeLlamarAlServicio() throws Exception {
        mockMvc.perform(post("/escenarios/disparo-coordinado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"armaId\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.excepcion").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.mensaje").value(org.hamcrest.Matchers.containsString("jugadorId")))
                .andExpect(jsonPath("$.campo").value("jugadorId"))
                .andExpect(jsonPath("$.errores.jugadorId").exists())
                .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void unCompaneroNoDisponibleResponde502YNoEscribe() throws Exception {
        Long jugadorId = jugadorRepository.save(new Jugador("Neo")).getId();
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));
        when(companeroClient.validar(eq(RolCompanero.A2), any(ReglaRequest.class)))
                .thenThrow(new CompaneroNoDisponibleException(RolCompanero.A2, 502, 503,
                        "No se pudo consultar a A2", "A2 no responde", null));

        mockMvc.perform(post("/escenarios/disparo-coordinado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jugadorId":%d,"armaId":%d}
                                """.formatted(jugadorId, fusil.getId())))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.excepcion").value("CompaneroNoDisponibleException"))
                .andExpect(jsonPath("$.rechazadoPor").value("A2"))
                .andExpect(jsonPath("$.statusRemoto").value(503))
                .andExpect(jsonPath("$.statusHttp").value(502))
                .andExpect(jsonPath("$.cuerpoRemoto").value("A2 no responde"))
                .andExpect(jsonPath("$.traceId").exists());

        org.junit.jupiter.api.Assertions.assertEquals(0, simulacionRepository.count());
    }

    private void aprobaciones() {
        when(companeroClient.validar(eq(RolCompanero.A2), any(ReglaRequest.class)))
                .thenReturn(new RespuestaCompanero(RolCompanero.A2, 200, "{\"permitido\":true}", "POST /r/a2"));
        when(companeroClient.validar(eq(RolCompanero.A3), any(ReglaRequest.class)))
                .thenReturn(new RespuestaCompanero(RolCompanero.A3, 200, "{\"permitido\":true}", "POST /r/a3"));
    }

    private Long extraerId(String json) {
        String fragmento = json.substring(json.indexOf("\"id\":") + 5);
        return Long.parseLong(fragmento.substring(0, fragmento.indexOf(',')));
    }
}

package py.edu.uc.lp3.mm.cs2.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.repository.ArmaRepository;
import py.edu.uc.lp3.mm.cs2.repository.FusilRepository;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RestApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArmaRepository armaRepository;

    @Autowired
    private FusilRepository fusilRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Test
    void creaUnFusilPorHttp() throws Exception {
        mockMvc.perform(post("/armas/fusiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"M4A1","precio":3100,"dano":33,"precision":80}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("M4A1"))
                .andExpect(jsonPath("$.tipoArma").value("fusil"))
                .andExpect(jsonPath("$.dano").value(33));

        mockMvc.perform(get("/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoArma").value("fusil"));
    }

    @Test
    void actualizaYeliminaUnFusil() throws Exception {
        Fusil fusil = fusilRepository.save(new Fusil(
                "M4A1", 0, 3100, 33, 80, 2.3, 9, 30, true, 2, 18, false));

        mockMvc.perform(put("/armas/fusiles/" + fusil.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"M4A1-S","dano":38}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("M4A1-S"))
                .andExpect(jsonPath("$.dano").value(38));

        mockMvc.perform(get("/armas/fusiles/" + fusil.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("M4A1-S"));

        mockMvc.perform(delete("/armas/fusiles/" + fusil.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/armas/fusiles/" + fusil.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void simulaUnDisparoYExponeElHistorial() throws Exception {
        Jugador jugador = jugadorRepository.save(new Jugador("Neo"));
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));

        mockMvc.perform(post("/simulacion/disparar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jugadorId":%d,"armaId":%d}
                                """.formatted(jugador.getId(), fusil.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vidaAntes").value(100))
                .andExpect(jsonPath("$.vidaDespues").value(64))
                .andExpect(jsonPath("$.danoAplicado").value(36))
                .andExpect(jsonPath("$.vivo").value(true));

        mockMvc.perform(get("/simulacion/historial/jugador/" + jugador.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jugador").value("Neo"))
                .andExpect(jsonPath("$[0].dano").value(36));
    }

    @Test
    void creaUnJugadorConBlindaje() throws Exception {
        mockMvc.perform(post("/jugadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Neo","blindaje":100}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Neo"))
                .andExpect(jsonPath("$.blindaje").value(100));
    }

    @Test
    void creaUnJugadorSinBlindaje() throws Exception {
        mockMvc.perform(post("/jugadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"SinChaleco"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("SinChaleco"))
                .andExpect(jsonPath("$.blindaje").value(0));
    }

    @Test
    void devuelve409CuandoElArmaNoTieneMunicion() throws Exception {
        Jugador jugador = jugadorRepository.save(new Jugador("Neo"));
        Fusil fusil = new Fusil("AK-47", 0, 2700, 36, 75, 2.5, 10, 1, true, 2, 20, false);
        fusil.setMunicionActual(0);
        fusil = fusilRepository.save(fusil);

        mockMvc.perform(post("/simulacion/disparar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jugadorId":%d,"armaId":%d}
                                """.formatted(jugador.getId(), fusil.getId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.excepcion").value("MunicionInsuficienteException"))
                .andExpect(jsonPath("$.mensaje").value("AK-47 no puede disparar: cargador insuficiente (0 balas, necesita 1)"));
    }

    @Test
    void devuelve409CuandoElJugadorYaEstaEliminado() throws Exception {
        Jugador jugador = new Jugador("Neo");
        jugador.recibirDisparo(100);
        jugador = jugadorRepository.save(jugador);
        Fusil fusil = fusilRepository.save(new Fusil(
                "AK-47", 0, 2700, 36, 75, 2.5, 10, 30, true, 2, 20, false));

        mockMvc.perform(post("/simulacion/disparar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jugadorId":%d,"armaId":%d}
                                """.formatted(jugador.getId(), fusil.getId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.excepcion").value("JugadorEliminadoException"))
                .andExpect(jsonPath("$.mensaje").value("El jugador Neo ya está muerto y no puede recibir más disparos"));
    }

    @Test
    void traduceLasExcepcionesDelDominio() throws Exception {
        long armasAntes = armaRepository.count();

        mockMvc.perform(get("/armas/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.excepcion").value("RecursoNoEncontradoException"))
                .andExpect(jsonPath("$.mensaje").value("No existe un arma con id 99999"));

        mockMvc.perform(post("/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"fusil","nombre":"Malo","precio":-1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.excepcion").value("DatosInvalidosException"))
                .andExpect(jsonPath("$.mensaje").value("El precio debe ser no negativo"));

        assertEquals(armasAntes, armaRepository.count());

        mockMvc.perform(get("/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value((int) armasAntes));
    }

    @Test
    void listaElementosUICapables() throws Exception {
        mockMvc.perform(get("/ui-capables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Fusil"))
                .andExpect(jsonPath("$[0].imagen").exists())
                .andExpect(jsonPath("$[0].mensaje").exists())
                .andExpect(jsonPath("$[1].tipo").value("Flash"));
    }
}

package py.edu.uc.lp3.mm.cs2.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReglasRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void elResumenPublicaLasDosReglas() throws Exception {
        mockMvc.perform(get("/api/reglas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municion").exists())
                .andExpect(jsonPath("$.vida").exists());
    }

    @Test
    void laConsultaDeMunicionAceptaYRechazaSinTocarLaBase() throws Exception {
        mockMvc.perform(post("/api/reglas/municion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carga\":1,\"dano\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permitido").value(true));

        mockMvc.perform(post("/api/reglas/municion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carga\":0,\"dano\":30}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.excepcion").value("MunicionInsuficienteException"));
    }

    @Test
    void losAliasesConLosOperationIdEsperadosPorUnCompaneroFuncionan() throws Exception {
        mockMvc.perform(post("/api/companeros/municion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carga\":1,\"dano\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permitido").value(true));

        mockMvc.perform(post("/api/companeros/danio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vida\":50,\"blindaje\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permitido").value(true));

        mockMvc.perform(post("/api/companeros/danio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vida\":0}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.excepcion").value("JugadorEliminadoException"));
    }

    @Test
    void laConsultaDeJugadorVivoAceptaYRechazaSinTocarLaBase() throws Exception {
        mockMvc.perform(post("/api/reglas/jugador-vivo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vida\":50,\"blindaje\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permitido").value(true))
                .andExpect(jsonPath("$.blindaje").value(20));

        mockMvc.perform(post("/api/reglas/jugador-vivo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vida\":0}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.excepcion").value("JugadorEliminadoException"));
    }
}

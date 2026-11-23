package py.edu.uc.lp3.mm.cs2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiDocumentationTest {

    @LocalServerPort
    private int puerto;

    @Test
    void elDocumentoOpenApiDescribeCrudSimulacionYErroresDeLasDosReglas() {
        String documento = RestClient.create()
                .get()
                .uri("http://localhost:" + puerto + "/v3/api-docs")
                .retrieve()
                .body(String.class);

        assertEquals(true, documento != null && documento.contains("\"openapi\""));
        assertTrue(documento.contains("/armas"));
        assertTrue(documento.contains("/jugadores"));
        assertTrue(documento.contains("/simulacion/disparar"));
        assertTrue(documento.contains("MunicionInsuficienteException"));
        assertTrue(documento.contains("JugadorEliminadoException"));
        assertTrue(documento.contains("simularDisparo"));
    }
}

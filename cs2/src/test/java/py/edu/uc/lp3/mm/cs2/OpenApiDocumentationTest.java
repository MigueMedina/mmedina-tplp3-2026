package py.edu.uc.lp3.mm.cs2;

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

        assertTrue(documento != null);
        assertTrue(documento.matches("(?s).*\"openapi\"\\s*:\\s*\"3\\.0\\.[0-9]+\".*"));
        assertTrue(documento.contains("/armas"));
        assertTrue(documento.contains("/jugadores"));
        assertTrue(documento.contains("/simulacion/disparar"));
        assertTrue(documento.contains("/escenarios/disparo-coordinado"));
        assertTrue(documento.contains("/api/reglas"));
        assertTrue(documento.contains("/api/reglas/municion"));
        assertTrue(documento.contains("/api/reglas/jugador-vivo"));
        assertTrue(documento.contains("/api/catalogo"));
        assertTrue(documento.contains("/api/companeros/municion"));
        assertTrue(documento.contains("/api/companeros/danio"));
        assertTrue(documento.contains("\"operationId\":\"municion\""));
        assertTrue(documento.contains("\"operationId\":\"danio\""));
        assertTrue(documento.contains("CatalogoArmaResponse"));
        assertTrue(documento.contains("ErrorResponse"));
        assertTrue(documento.contains("ErrorCompaneroResponse"));
        assertTrue(documento.contains("ErrorValidacionResponse"));
        assertTrue(documento.contains("MunicionInsuficienteException"));
        assertTrue(documento.contains("JugadorEliminadoException"));
        assertTrue(documento.contains("simularDisparo"));
        assertTrue(documento.contains("validarMunicion"));
        assertTrue(documento.contains("validarJugadorVivo"));
    }
}

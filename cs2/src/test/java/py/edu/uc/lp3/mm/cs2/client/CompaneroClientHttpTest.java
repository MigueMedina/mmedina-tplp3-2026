package py.edu.uc.lp3.mm.cs2.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.sun.net.httpserver.HttpServer;

class CompaneroClientHttpTest {

    @Test
    void resuelveElOpenApiYConsultaAlCompaneroConServidorHttpReal() throws IOException {
        AtomicReference<String> cuerpoRecibido = new AtomicReference<>();
        HttpServer servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        String base = "http://127.0.0.1:" + servidor.getAddress().getPort();

        servidor.createContext("/v3/api-docs", intercambio -> {
            String documento = """
                    {"openapi":"3.1.0","servers":[{"url":"http://servidor-remoto.invalid"}],
                     "paths":{"/api/reglas/municion":{"post":{"operationId":"municion"}}}}
                    """;
            responder(intercambio, documento);
        });
        servidor.createContext("/api/reglas/municion", intercambio -> {
            cuerpoRecibido.set(new String(intercambio.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            responder(intercambio, "{\"permitido\":true,\"cargadorAntes\":3,\"cargadorDespues\":2}");
        });
        servidor.start();

        try {
            ApiClientesProperties propiedades = new ApiClientesProperties();
            propiedades.getA2().setBaseUrl(base);
            propiedades.getA2().setOpenapiUrl(base + "/v3/api-docs");
            propiedades.getA2().setOperationId("municion");
            propiedades.getA2().getCuerpo().put("cargador", "cargador");
            propiedades.getA2().getCuerpo().put("balasPorDisparo", "balasPorDisparo");
            propiedades.getA2().getCuerpo().put("disparos", "disparos");

            RestClient.Builder constructor = RestClient.builder();
            CompaneroClient cliente = new CompaneroClient(
                    propiedades, new OpenApiOperationResolver(constructor), constructor);
            RespuestaCompanero respuesta = cliente.validar(RolCompanero.A2, new ReglaRequest(Map.of(
                    "cargador", 3,
                    "balasPorDisparo", 1,
                    "disparos", 1)));

            assertTrue(respuesta.aprobo());
            assertEquals("POST /api/reglas/municion", respuesta.operacion());
            assertTrue(cuerpoRecibido.get().contains("\"cargador\":3"));
            assertTrue(cuerpoRecibido.get().contains("\"balasPorDisparo\":1"));
            assertTrue(cuerpoRecibido.get().contains("\"disparos\":1"));
        } finally {
            servidor.stop(0);
        }
    }

    private void responder(com.sun.net.httpserver.HttpExchange intercambio, String cuerpo) throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        intercambio.getResponseHeaders().set("Content-Type", "application/json");
        intercambio.sendResponseHeaders(200, bytes.length);
        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(bytes);
        }
    }
}

package py.edu.uc.lp3.mm.cs2.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import py.edu.uc.lp3.mm.cs2.exception.CompanneroRechazoException;
import py.edu.uc.lp3.mm.cs2.exception.CompaneroNoDisponibleException;

class CompaneroClientTest {

    @Test
    void resuelveLaRutaDelOpenApiYConsultaAA2Fabricio() {
        RestClient.Builder constructor = RestClient.builder();
        MockRestServiceServer servidor = MockRestServiceServer.bindTo(constructor).build();
        ApiClientesProperties propiedades = propiedades("municion");
        CompaneroClient cliente = new CompaneroClient(
                propiedades, new OpenApiOperationResolver(constructor), constructor);

        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/v3/api-docs"))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("municion", "/api/reglas/municion"), MediaType.APPLICATION_JSON));
        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/api/reglas/municion"))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content()
                        .json("{\"cargador\":3,\"balasPorDisparo\":1,\"disparos\":2}"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess("{\"permitido\":true}", MediaType.APPLICATION_JSON));

        RespuestaCompanero respuesta = cliente.validar(RolCompanero.A2, new ReglaRequest(Map.of(
                "cargador", 3,
                "balasPorDisparo", 1,
                "disparos", 2)));

        assertEquals(200, respuesta.status());
        assertEquals("POST /api/reglas/municion", respuesta.operacion());
        assertEquals("{\"permitido\":true}", respuesta.cuerpo());
        servidor.verify();
    }

    @Test
    void atribuyeElRechazoAA3JoaquinYCargaElCuerpoRemoto() {
        RestClient.Builder constructor = RestClient.builder();
        MockRestServiceServer servidor = MockRestServiceServer.bindTo(constructor).build();
        ApiClientesProperties propiedades = propiedades("danio");
        CompaneroClient cliente = new CompaneroClient(
                propiedades, new OpenApiOperationResolver(constructor), constructor);

        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a3.test/v3/api-docs"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("danio", "/api/reglas/danio"), MediaType.APPLICATION_JSON));
        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a3.test/api/reglas/danio"))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content()
                        .json("""
                                {
                                  "tirador": "A1-MigueMedina",
                                  "equipoTirador": "TERRORISTAS",
                                  "objetivo": "Halcón",
                                  "equipoObjetivo": "ANTITERRORISTAS",
                                  "vidaObjetivo": 100,
                                  "blindajeObjetivo": 50,
                                  "danio": 36
                                }
                                """))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"excepcion\":\"JugadorEliminadoException\"}"));

        CompanneroRechazoException excepcion = assertThrows(CompanneroRechazoException.class,
                () -> cliente.validar(RolCompanero.A3, new ReglaRequest(Map.of(
                        "tirador", "A1-MigueMedina",
                        "equipoTirador", "TERRORISTAS",
                        "objetivo", "Halcón",
                        "equipoObjetivo", "ANTITERRORISTAS",
                        "vidaObjetivo", 100,
                        "blindajeObjetivo", 50,
                        "danio", 36))));

        assertEquals(RolCompanero.A3, excepcion.getRechazadoPor());
        assertEquals(422, excepcion.getStatusRemoto());
        assertEquals("{\"excepcion\":\"JugadorEliminadoException\"}", excepcion.getCuerpoRemoto());
        servidor.verify();
    }

    @Test
    void fallaSiFaltaElMapeoDeUnCampoRemoto() {
        RestClient.Builder constructor = RestClient.builder();
        MockRestServiceServer servidor = MockRestServiceServer.bindTo(constructor).build();
        ApiClientesProperties propiedades = propiedades("municion");
        propiedades.getA2().getCuerpo().remove("disparos");
        CompaneroClient cliente = new CompaneroClient(
                propiedades, new OpenApiOperationResolver(constructor), constructor);

        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/v3/api-docs"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("municion", "/api/reglas/municion"), MediaType.APPLICATION_JSON));

        IllegalStateException excepcion = assertThrows(IllegalStateException.class,
                () -> cliente.validar(RolCompanero.A2, new ReglaRequest(Map.of(
                        "cargador", 3,
                        "balasPorDisparo", 1,
                        "disparos", 2))));

        assertTrue(excepcion.getMessage().contains("disparos"));
        servidor.verify();
    }

    @Test
    void elResolverCacheaElOpenApiYPuedeInvalidarse() {
        RestClient.Builder constructor = RestClient.builder();
        MockRestServiceServer servidor = MockRestServiceServer.bindTo(constructor).build();
        ApiClientesProperties propiedades = propiedades("municion");
        OpenApiOperationResolver resolver = new OpenApiOperationResolver(constructor);

        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/v3/api-docs"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("municion", "/api/reglas/municion"), MediaType.APPLICATION_JSON));
        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/v3/api-docs"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("municion", "/api/reglas/nueva"), MediaType.APPLICATION_JSON));
        assertEquals("/api/reglas/municion", resolver.resolver(propiedades.getA2(), RolCompanero.A2).ruta());
        assertEquals("/api/reglas/municion", resolver.resolver(propiedades.getA2(), RolCompanero.A2).ruta());

        resolver.invalidar();
        assertEquals("/api/reglas/nueva", resolver.resolver(propiedades.getA2(), RolCompanero.A2).ruta());
        servidor.verify();
    }

    @Test
    void clasificaComoNoDisponibleUnError5xxDelCompanero() {
        RestClient.Builder constructor = RestClient.builder();
        MockRestServiceServer servidor = MockRestServiceServer.bindTo(constructor).build();
        ApiClientesProperties propiedades = propiedades("municion");
        CompaneroClient cliente = new CompaneroClient(
                propiedades, new OpenApiOperationResolver(constructor), constructor);

        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/v3/api-docs"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("municion", "/api/reglas/municion"), MediaType.APPLICATION_JSON));
        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/api/reglas/municion"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withStatus(HttpStatus.SERVICE_UNAVAILABLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"caido\"}"));

        CompaneroNoDisponibleException excepcion = assertThrows(CompaneroNoDisponibleException.class,
                () -> cliente.validar(RolCompanero.A2, new ReglaRequest(Map.of(
                        "cargador", 3,
                        "balasPorDisparo", 1,
                        "disparos", 2))));

        assertEquals(502, excepcion.getStatusHttp());
        assertEquals(503, excepcion.getStatusRemoto());
        servidor.verify();
    }

    private ApiClientesProperties propiedades(String operationId) {
        ApiClientesProperties propiedades = new ApiClientesProperties();
        String host = operationId.equals("municion") ? "http://a2.test" : "http://a3.test";
        propiedades.getA2().setBaseUrl(host);
        propiedades.getA2().setOperationId(operationId);
        propiedades.getA2().getCuerpo().put("cargador", "cargador");
        propiedades.getA2().getCuerpo().put("balasPorDisparo", "balasPorDisparo");
        propiedades.getA2().getCuerpo().put("disparos", "disparos");
        propiedades.getA3().setBaseUrl(host);
        propiedades.getA3().setOperationId(operationId);
        propiedades.getA3().getCuerpo().put("tirador", "tirador");
        propiedades.getA3().getCuerpo().put("equipoTirador", "equipoTirador");
        propiedades.getA3().getCuerpo().put("objetivo", "objetivo");
        propiedades.getA3().getCuerpo().put("equipoObjetivo", "equipoObjetivo");
        propiedades.getA3().getCuerpo().put("vidaObjetivo", "vidaObjetivo");
        propiedades.getA3().getCuerpo().put("blindajeObjetivo", "blindajeObjetivo");
        propiedades.getA3().getCuerpo().put("danio", "danio");
        return propiedades;
    }

    private String documento(String operationId, String ruta) {
        return "{\"openapi\":\"3.1.0\",\"servers\":[{\"url\":\""
                + (operationId.equals("municion") ? "http://a2.test" : "http://a3.test")
                + "\"}],\"paths\":{\"" + ruta + "\":{\"post\":{\"operationId\":\"" + operationId + "\"}}}}";
    }
}

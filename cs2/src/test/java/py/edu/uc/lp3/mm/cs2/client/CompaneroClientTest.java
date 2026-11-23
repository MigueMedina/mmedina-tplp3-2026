package py.edu.uc.lp3.mm.cs2.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import py.edu.uc.lp3.mm.cs2.exception.CompanneroRechazoException;

class CompaneroClientTest {

    @Test
    void resuelveLaRutaDesdelOpenApiYConsultaAA2() {
        RestClient.Builder constructor = RestClient.builder();
        MockRestServiceServer servidor = MockRestServiceServer.bindTo(constructor).build();
        ApiClientesProperties propiedades = propiedades("validarMunicion");
        CompaneroClient cliente = new CompaneroClient(
                propiedades, new OpenApiOperationResolver(constructor), constructor);

        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/v3/api-docs"))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("validarMunicion", "/reglas/municion"), MediaType.APPLICATION_JSON));
        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a2.test/reglas/municion"))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content()
                        .json("{\"jugadorId\":1,\"armaId\":2,\"dano\":36}"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess("{\"aprobado\":true}", MediaType.APPLICATION_JSON));

        RespuestaCompanero respuesta = cliente.validar(RolCompanero.A2, new ReglaRequest(1L, 2, 36));

        assertEquals(200, respuesta.status());
        assertEquals("POST /reglas/municion", respuesta.operacion());
        assertEquals("{\"aprobado\":true}", respuesta.cuerpo());
        servidor.verify();
    }

    @Test
    void atribuyeElRechazoAA3YCargaElCuerpoRemoto() {
        RestClient.Builder constructor = RestClient.builder();
        MockRestServiceServer servidor = MockRestServiceServer.bindTo(constructor).build();
        ApiClientesProperties propiedades = propiedades("validarJugadorVivo");
        CompaneroClient cliente = new CompaneroClient(
                propiedades, new OpenApiOperationResolver(constructor), constructor);

        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a3.test/v3/api-docs"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withSuccess(documento("validarJugadorVivo", "/reglas/vida"), MediaType.APPLICATION_JSON));
        servidor.expect(org.springframework.test.web.client.match.MockRestRequestMatchers
                        .requestTo("http://a3.test/reglas/vida"))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withStatus(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"excepcion\":\"JugadorEliminadoException\"}"));

        CompanneroRechazoException excepcion = assertThrows(CompanneroRechazoException.class,
                () -> cliente.validar(RolCompanero.A3, new ReglaRequest(1L, 2, 36)));

        assertEquals(RolCompanero.A3, excepcion.getRechazadoPor());
        assertEquals(409, excepcion.getStatusRemoto());
        assertEquals("{\"excepcion\":\"JugadorEliminadoException\"}", excepcion.getCuerpoRemoto());
        servidor.verify();
    }

    private ApiClientesProperties propiedades(String operationId) {
        ApiClientesProperties propiedades = new ApiClientesProperties();
        String host = operationId.equals("validarMunicion") ? "http://a2.test" : "http://a3.test";
        propiedades.getA2().setBaseUrl(host);
        propiedades.getA2().setOperationId(operationId);
        propiedades.getA3().setBaseUrl(host);
        propiedades.getA3().setOperationId(operationId);
        return propiedades;
    }

    private String documento(String operationId, String ruta) {
        return "{\"openapi\":\"3.1.0\",\"servers\":[{\"url\":\""
                + (operationId.equals("validarMunicion") ? "http://a2.test" : "http://a3.test")
                + "\"}],\"paths\":{\"" + ruta + "\":{\"post\":{\"operationId\":\"" + operationId + "\"}}}}";
    }
}

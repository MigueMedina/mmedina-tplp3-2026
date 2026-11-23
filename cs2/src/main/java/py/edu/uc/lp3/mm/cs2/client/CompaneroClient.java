package py.edu.uc.lp3.mm.cs2.client;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import py.edu.uc.lp3.mm.cs2.exception.CompanneroRechazoException;

@Service
public class CompaneroClient {

    private final ApiClientesProperties properties;
    private final OpenApiOperationResolver resolver;
    private final RestClient restClient;

    public CompaneroClient(ApiClientesProperties properties,
                           OpenApiOperationResolver resolver,
                           RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.resolver = resolver;
        this.restClient = restClientBuilder.build();
    }

    public RespuestaCompanero validar(RolCompanero rol, ReglaRequest regla) {
        ApiClientesProperties.Companero configuracion = properties.de(rol);
        OpenApiOperationResolver.OperacionRemota operacion;
        try {
            operacion = resolver.resolver(configuracion, rol);
        } catch (RuntimeException excepcion) {
            throw new CompanneroRechazoException(rol, 503,
                    "No se pudo leer el OpenAPI de " + rol.getEtiqueta() + ": " + excepcion.getMessage(),
                    excepcion);
        }

        Map<String, Object> cuerpo = construirCuerpo(configuracion, regla);
        String url = construirUrl(configuracion, operacion);
        String descripcionOperacion = operacion.metodo() + " " + operacion.ruta();

        try {
            ResponseEntity<String> respuesta = restClient.method(HttpMethod.valueOf(operacion.metodo()))
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpo)
                    .retrieve()
                    .toEntity(String.class);

            if (!respuesta.getStatusCode().is2xxSuccessful()) {
                throw new CompanneroRechazoException(rol, respuesta.getStatusCode().value(),
                        respuesta.getBody());
            }
            return new RespuestaCompanero(rol, respuesta.getStatusCode().value(),
                    respuesta.getBody(), descripcionOperacion);
        } catch (CompanneroRechazoException excepcion) {
            throw excepcion;
        } catch (RestClientResponseException excepcion) {
            throw new CompanneroRechazoException(rol, excepcion.getStatusCode().value(),
                    excepcion.getResponseBodyAsString(), excepcion);
        } catch (RestClientException excepcion) {
            throw new CompanneroRechazoException(rol, 503,
                    rol.getEtiqueta() + " no respondió: " + excepcion.getMessage(), excepcion);
        }
    }

    private Map<String, Object> construirCuerpo(ApiClientesProperties.Companero configuracion,
                                                ReglaRequest regla) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        ApiClientesProperties.Cuerpo campos = configuracion.getCuerpo();
        if (regla.jugadorId() != null && campos.getJugadorId() != null) {
            cuerpo.put(campos.getJugadorId(), regla.jugadorId());
        }
        if (regla.armaId() != null && campos.getArmaId() != null) {
            cuerpo.put(campos.getArmaId(), regla.armaId());
        }
        if (regla.dano() != null && campos.getDano() != null) {
            cuerpo.put(campos.getDano(), regla.dano());
        }
        return cuerpo;
    }

    private String construirUrl(ApiClientesProperties.Companero configuracion,
                                OpenApiOperationResolver.OperacionRemota operacion) {
        String servidor = operacion.servidor();
        String base = servidor != null && servidor.startsWith("http")
                ? servidor
                : configuracion.getBaseUrl();
        if (base == null || base.isBlank()) {
            throw new IllegalStateException("Falta la URL base de " + configuracion.getOperationId());
        }
        String ruta = operacion.ruta().startsWith("/") ? operacion.ruta() : "/" + operacion.ruta();
        return base.endsWith("/") ? base.substring(0, base.length() - 1) + ruta : base + ruta;
    }
}

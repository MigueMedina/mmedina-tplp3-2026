package py.edu.uc.lp3.mm.cs2.client;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;

@Component
public class OpenApiOperationResolver {

    private static final String[] METODOS_HTTP = {"get", "post", "put", "patch", "delete"};
    private static final Duration TTL = Duration.ofMinutes(5);

    private final RestClient restClient;
    private final Map<String, EntradaCache> cache = new ConcurrentHashMap<>();

    public OpenApiOperationResolver(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public OperacionRemota resolver(ApiClientesProperties.Companero configuracion, RolCompanero rol) {
        String operationId = configuracion.getOperationId();
        if (operationId == null || operationId.isBlank()) {
            throw new IllegalArgumentException("Falta el operationId de " + rol.getEtiqueta());
        }
        String urlDocumento = configuracion.getOpenapiUrl();
        String clave = rol.name() + "|" + operationId + "|" + urlDocumento;
        EntradaCache entrada = cache.get(clave);
        if (entrada != null && entrada.cargada().isAfter(Instant.now().minus(TTL))) {
            return entrada.operacion();
        }
        OperacionRemota operacion = consultar(urlDocumento, operationId);
        cache.put(clave, new EntradaCache(operacion, Instant.now()));
        return operacion;
    }

    public void invalidar() {
        cache.clear();
    }

    private OperacionRemota consultar(String urlDocumento, String operationId) {
        JsonNode documento = restClient.get()
                .uri(urlDocumento)
                .retrieve()
                .body(JsonNode.class);

        if (documento == null || !documento.has("paths")) {
            throw new IllegalStateException("El documento OpenAPI " + urlDocumento + " no tiene Paths");
        }

        Iterator<Map.Entry<String, JsonNode>> rutas = documento.path("paths").properties().iterator();
        while (rutas.hasNext()) {
            Map.Entry<String, JsonNode> ruta = rutas.next();
            for (String metodo : METODOS_HTTP) {
                JsonNode operacion = ruta.getValue().path(metodo);
                if (!operacion.isMissingNode()
                        && operationId.equals(operacion.path("operationId").asText())) {
                    return new OperacionRemota(
                            metodo.toUpperCase(Locale.ROOT),
                            ruta.getKey(),
                            servidorDe(documento));
                }
            }
        }

        throw new IllegalStateException("No se encontró el operationId '" + operationId + "' en " + urlDocumento);
    }

    private String servidorDe(JsonNode documento) {
        JsonNode servidores = documento.path("servers");
        if (servidores.isArray() && !servidores.isEmpty()) {
            return servidores.get(0).path("url").asText("");
        }
        return "";
    }

    public record OperacionRemota(String metodo, String ruta, String servidor) {
    }

    private record EntradaCache(OperacionRemota operacion, Instant cargada) {
    }
}

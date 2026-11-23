package py.edu.uc.lp3.mm.cs2.client;

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

    private final RestClient restClient;
    private final Map<String, OperacionRemota> cache = new ConcurrentHashMap<>();

    public OpenApiOperationResolver(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public OperacionRemota resolver(ApiClientesProperties.Companero configuracion, RolCompanero rol) {
        String urlDocumento = configuracion.getOpenapiUrl();
        String clave = rol.name() + "|" + configuracion.getOperationId() + "|" + urlDocumento;
        return cache.computeIfAbsent(clave, ignored -> consultar(urlDocumento, configuracion.getOperationId()));
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
}

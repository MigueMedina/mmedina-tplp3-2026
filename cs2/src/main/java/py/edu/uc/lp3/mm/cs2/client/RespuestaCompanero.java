package py.edu.uc.lp3.mm.cs2.client;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public record RespuestaCompanero(RolCompanero rol, int status, String cuerpo, String operacion) {

    private static final ObjectMapper LECTOR = new ObjectMapper();

    public boolean aprobo() {
        if (status < 200 || status > 299 || cuerpo == null || cuerpo.isBlank()) {
            return false;
        }
        try {
            JsonNode documento = LECTOR.readTree(cuerpo);
            return documento.path("permitido").asBoolean(false);
        } catch (Exception excepcion) {
            return false;
        }
    }
}

package py.edu.uc.lp3.mm.cs2.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apis")
public class ApiClientesProperties {

    private final Companero a2 = new Companero();
    private final Companero a3 = new Companero();

    public Companero getA2() {
        return a2;
    }

    public Companero getA3() {
        return a3;
    }

    public Companero de(RolCompanero rol) {
        return rol == RolCompanero.A2 ? a2 : a3;
    }

    public static class Companero {

        private String baseUrl;
        private String openapiUrl;
        private String operationId;
        private String metodo = "POST";
        private final Cuerpo cuerpo = new Cuerpo();

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getOpenapiUrl() {
            return openapiUrl == null || openapiUrl.isBlank()
                    ? baseUrl + "/v3/api-docs"
                    : openapiUrl;
        }

        public void setOpenapiUrl(String openapiUrl) {
            this.openapiUrl = openapiUrl;
        }

        public String getOperationId() {
            return operationId;
        }

        public void setOperationId(String operationId) {
            this.operationId = operationId;
        }

        public String getMetodo() {
            return metodo == null || metodo.isBlank() ? "POST" : metodo.toUpperCase();
        }

        public void setMetodo(String metodo) {
            this.metodo = metodo;
        }

        public Cuerpo getCuerpo() {
            return cuerpo;
        }
    }

    public static class Cuerpo {

        private String jugadorId = "jugadorId";
        private String armaId = "armaId";
        private String dano = "dano";

        public String getJugadorId() {
            return jugadorId;
        }

        public void setJugadorId(String jugadorId) {
            this.jugadorId = jugadorId;
        }

        public String getArmaId() {
            return armaId;
        }

        public void setArmaId(String armaId) {
            this.armaId = armaId;
        }

        public String getDano() {
            return dano;
        }

        public void setDano(String dano) {
            this.dano = dano;
        }
    }
}

package py.edu.uc.lp3.mm.cs2.client;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apis")
public class ApiClientesProperties {

    private final Companero a2 = new Companero("http://localhost:8081", "municion");
    private final Companero a3 = new Companero("http://localhost:8082", "danio");
    private Duration connectTimeout = Duration.ofSeconds(2);
    private Duration readTimeout = Duration.ofSeconds(5);

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

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
        private final Map<String, String> cuerpo = new LinkedHashMap<>();

        public Companero() {
        }

        public Companero(String baseUrl, String operationId) {
            this.baseUrl = baseUrl;
            this.operationId = operationId;
        }

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

        public Map<String, String> getCuerpo() {
            return cuerpo;
        }

        public void setCuerpo(Map<String, String> cuerpo) {
            this.cuerpo.clear();
            if (cuerpo != null) {
                this.cuerpo.putAll(cuerpo);
            }
        }
    }
}

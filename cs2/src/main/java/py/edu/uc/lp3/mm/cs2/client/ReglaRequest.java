package py.edu.uc.lp3.mm.cs2.client;

import java.util.LinkedHashMap;
import java.util.Map;

public record ReglaRequest(Map<String, Object> valores) {

    public ReglaRequest {
        valores = valores == null ? Map.of() : new LinkedHashMap<>(valores);
    }
}

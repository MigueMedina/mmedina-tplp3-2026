package py.edu.uc.lp3.mm.cs2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.mm.cs2.rest.dto.CatalogoArmaResponse;

class CatalogoServiceTest {

    private final CatalogoService catalogoService = new CatalogoService();

    @Test
    void elCatalogoEsEstableYTraeLosModelosClave() {
        List<CatalogoArmaResponse> catalogo = catalogoService.listar();

        assertEquals(14, catalogo.size());
        Set<String> modelos = catalogo.stream()
                .map(CatalogoArmaResponse::modelo)
                .collect(Collectors.toSet());
        assertTrue(modelos.containsAll(List.of("AK47", "M4A4", "AWP", "SSG08", "HE_GRANADA")));
        assertTrue(catalogo.stream().anyMatch(arma -> "TT".equals(arma.equipo())));
        assertTrue(catalogo.stream().anyMatch(arma -> "CT".equals(arma.equipo())));
        assertTrue(catalogo.stream().allMatch(arma -> arma.precio() > 0 && arma.alcanceMetros() > 0));
    }
}

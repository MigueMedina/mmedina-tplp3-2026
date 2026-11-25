package py.edu.uc.lp3.mm.cs2.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.rest.dto.CatalogoArmaResponse;
import py.edu.uc.lp3.mm.cs2.service.CatalogoService;

@RestController
@RequestMapping(ApiPaths.CATALOGO)
@Tag(name = "Catálogo", description = "Armas disponibles con stats orientativas para que A2/A3 descubran el contrato de A1.")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    @Operation(
            operationId = "listarCatalogo",
            summary = "Listar el catálogo CS2 de A1",
            description = "Devuelve modelos, equipo comprador, precio, daño, cargador, reserva, ráfaga, "
                    + "alcance y penetración de blindaje. Los valores son propios de A1 y sirven como "
                    + "referencia documental; no sustituyen al arma que el cliente elija al crearla.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catálogo completo.",
                    content = @Content(schema = @Schema(implementation = CatalogoArmaResponse.class)))
    })
    public List<CatalogoArmaResponse> listar() {
        return catalogoService.listar();
    }
}

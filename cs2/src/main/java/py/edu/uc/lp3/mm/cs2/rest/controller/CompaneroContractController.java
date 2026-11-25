package py.edu.uc.lp3.mm.cs2.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.rest.dto.ErrorResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaConsultaRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaJugadorVivoResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaMunicionResponse;
import py.edu.uc.lp3.mm.cs2.service.ReglasService;

@RestController
@RequestMapping(ApiPaths.COMPANEROS)
@Tag(name = "Contrato para compañeros", description = "Aliases con los operationId que A1 espera consumir de A2/A3: munición y daño.")
public class CompaneroContractController {

    private final ReglasService reglasService;

    public CompaneroContractController(ReglasService reglasService) {
        this.reglasService = reglasService;
    }

    @PostMapping("/municion")
    @Operation(
            operationId = "municion",
            summary = "Alias de munición con el operationId que A1 espera en A2",
            description = "Equivale a validarMunicion. Recibe carga y dano; no lee ni cambia la base.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hay munición suficiente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Cargador vacío.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReglaMunicionResponse municion(@Valid @RequestBody ReglaConsultaRequest request) {
        return reglasService.validarMunicion(request);
    }

    @PostMapping("/danio")
    @Operation(
            operationId = "danio",
            summary = "Alias de daño con el operationId que A1 espera en A3",
            description = "Equivale a validarJugadorVivo: comprueba que el objetivo pueda recibir daño. "
                    + "No calcula daño ni modifica la base.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "El objetivo puede recibir daño."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "El objetivo ya fue eliminado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReglaJugadorVivoResponse danio(@Valid @RequestBody ReglaConsultaRequest request) {
        return reglasService.validarJugadorVivo(request);
    }
}

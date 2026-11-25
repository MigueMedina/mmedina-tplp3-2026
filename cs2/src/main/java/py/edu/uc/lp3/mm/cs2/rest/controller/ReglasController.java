package py.edu.uc.lp3.mm.cs2.rest.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping(ApiPaths.REGLAS)
@Tag(name = "Reglas", description = "Consulta stateless de las dos reglas, sin repositorios ni escrituras en H2.")
public class ReglasController {

    private final ReglasService reglasService;

    public ReglasController(ReglasService reglasService) {
        this.reglasService = reglasService;
    }

    @GetMapping
    @Operation(operationId = "listarReglas", summary = "Las dos reglas en una frase cada una")
    public Map<String, String> resumen() {
        return Map.of("municion", ReglasService.REGLA_MUNICION, "vida", ReglasService.REGLA_VIDA);
    }

    @PostMapping("/municion")
    @Operation(
            operationId = "validarMunicion",
            summary = "Validar munición suficiente para un disparo",
            description = "Recibe el estado en el JSON. No lee ni cambia la base: 200 si hay al menos una bala, "
                    + "409 si el cargador está vacío.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Munición suficiente."),
            @ApiResponse(responseCode = "400", description = "Faltan campos o son imposibles.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                     examples = @ExampleObject(name = "DatosInvalidosException", value = """
                            {"excepcion":"DatosInvalidosException","mensaje":"La carga del cargador no puede ser negativa"}
                            """))),
            @ApiResponse(responseCode = "409", description = "El cargador está vacío.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                     examples = @ExampleObject(name = "MunicionInsuficienteException", value = """
                            {"excepcion":"MunicionInsuficienteException","mensaje":"No hay munición suficiente: cargador en 0 y se necesita 1 bala"}
                            """)))
    })
    public ReglaMunicionResponse validarMunicion(@Valid @RequestBody ReglaConsultaRequest request) {
        return reglasService.validarMunicion(request);
    }

    @PostMapping("/jugador-vivo")
    @Operation(
            operationId = "validarJugadorVivo",
            summary = "Validar que el jugador siga vivo",
            description = "Recibe el estado en el JSON. No lee ni cambia la base: 200 con vida mayor que 0, "
                    + "409 si el jugador ya tiene vida 0.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "El jugador sigue vivo."),
            @ApiResponse(responseCode = "400", description = "Vida o blindaje fuera de rango.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                     examples = @ExampleObject(name = "DatosInvalidosException", value = """
                            {"excepcion":"DatosInvalidosException","mensaje":"La vida del jugador debe estar entre 0 y 100"}
                            """))),
            @ApiResponse(responseCode = "409", description = "El jugador ya fue eliminado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                     examples = @ExampleObject(name = "JugadorEliminadoException", value = """
                            {"excepcion":"JugadorEliminadoException","mensaje":"El jugador ya fue eliminado: vida 0"}
                            """)))
    })
    public ReglaJugadorVivoResponse validarJugadorVivo(@Valid @RequestBody ReglaConsultaRequest request) {
        return reglasService.validarJugadorVivo(request);
    }
}

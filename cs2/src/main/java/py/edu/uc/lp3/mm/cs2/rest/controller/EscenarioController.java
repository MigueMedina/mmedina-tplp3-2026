package py.edu.uc.lp3.mm.cs2.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioResponse;
import py.edu.uc.lp3.mm.cs2.service.EscenarioService;

@RestController
@RequestMapping(ApiPaths.ESCENARIOS)
@Tag(name = "Escenario coordinado", description = "Escenario POO-09: exige las aprobaciones de A2 y A3 antes de persistir.")
public class EscenarioController {

    private final EscenarioService escenarioService;

    public EscenarioController(EscenarioService escenarioService) {
        this.escenarioService = escenarioService;
    }

    @PostMapping("/disparo-coordinado")
    @Operation(
            operationId = "ejecutarDisparoCoordinado",
            summary = "Ejecutar el escenario con doble aprobación",
            description = "A1 consulta primero a A2 y después a A3 usando sus OpenAPI. Sólo si ambos aceptan "
                    + "se guarda la jugada en el H2 local; si uno rechaza, la base queda igual y el cuerpo "
                    + "indica `rechazadoPor`.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ambas aprobaciones: jugada y escenario persistidos."),
            @ApiResponse(responseCode = "400", description = "Faltan ids o el arma no es ofensiva."),
            @ApiResponse(responseCode = "409", description = "Rechazo atribuido a A2 o A3; H2 sin cambios.",
                    content = @Content(examples = @ExampleObject(value = """
                            {"timestamp":"2026-11-22T12:00:00","status":409,"error":"Conflict",
                             "excepcion":"CompanneroRechazoException",
                             "mensaje":"Rechazo de A3 (HTTP 409)",
                             "rechazadoPor":"A3","statusRemoto":409,
                             "cuerpoRemoto":"{\\"excepcion\\":\\"JugadorEliminadoException\\"}",
                             "path":"/escenarios/disparo-coordinado"}
                            """)))
    })
    public ResponseEntity<EscenarioResponse> ejecutar(@RequestBody EscenarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(escenarioService.ejecutar(request));
    }

    @GetMapping
    @Operation(operationId = "listarEscenarios", summary = "Listar escenarios aprobados y persistidos")
    public List<EscenarioResponse> listar() {
        return escenarioService.listar();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarEscenario", summary = "Buscar un escenario por id")
    @ApiResponse(responseCode = "404", description = "No existe el escenario indicado.")
    public EscenarioResponse buscar(@PathVariable Long id) {
        return escenarioService.buscar(id);
    }
}

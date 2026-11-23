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
import py.edu.uc.lp3.mm.cs2.rest.dto.DisparoRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.SimulacionResponse;
import py.edu.uc.lp3.mm.cs2.service.SimulacionService;

@RestController
@RequestMapping(ApiPaths.SIMULACION)
@Tag(name = "Simulación", description = "Disparo persistido y reglas de munición, chaleco y jugador eliminado.")
public class SimulacionController {

    private final SimulacionService simulacionService;

    public SimulacionController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    @PostMapping("/disparar")
    @Operation(
            operationId = "simularDisparo",
            summary = "Disparar un arma contra un jugador",
            description = "Descuenta una bala, aplica el chaleco que absorbe hasta el 50% del daño y persiste la fila "
                    + "en `simulaciones`. Devuelve 409 cuando el cargador está vacío o el jugador ya está muerto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Disparo aplicado e historial persistido."),
            @ApiResponse(responseCode = "400", description = "Faltan ids o el arma no es ofensiva."),
            @ApiResponse(responseCode = "404", description = "No existe el jugador o el arma indicada."),
            @ApiResponse(responseCode = "409", description = "Regla de negocio incumplida: munición insuficiente o jugador eliminado.",
                    content = @Content(examples = {
                            @ExampleObject(name = "MunicionInsuficienteException", value = """
                                    {"timestamp":"2026-11-22T12:00:00","status":409,"error":"Conflict",
                                     "excepcion":"MunicionInsuficienteException",
                                     "mensaje":"AK-47 no puede disparar: cargador insuficiente (0 balas, necesita 1)",
                                     "path":"/simulacion/disparar"}
                                    """),
                            @ExampleObject(name = "JugadorEliminadoException", value = """
                                    {"timestamp":"2026-11-22T12:00:00","status":409,"error":"Conflict",
                                     "excepcion":"JugadorEliminadoException",
                                     "mensaje":"El jugador Neo ya está muerto y no puede recibir más disparos",
                                     "path":"/simulacion/disparar"}
                                    """)
                    }))
    })
    public ResponseEntity<SimulacionResponse> disparar(@RequestBody DisparoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(simulacionService.disparar(request));
    }

    @GetMapping("/historial")
    @Operation(operationId = "listarHistorial", summary = "Listar todo el historial de disparos")
    public List<SimulacionResponse> historial() {
        return simulacionService.historial();
    }

    @GetMapping("/historial/jugador/{jugadorId}")
    @Operation(operationId = "listarHistorialPorJugador", summary = "Listar el historial de un jugador")
    @ApiResponse(responseCode = "404", description = "No existe el jugador indicado.")
    public List<SimulacionResponse> historialDeJugador(@PathVariable Long jugadorId) {
        return simulacionService.historialDeJugador(jugadorId);
    }
}

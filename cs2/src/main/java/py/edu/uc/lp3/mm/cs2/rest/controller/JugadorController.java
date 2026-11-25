package py.edu.uc.lp3.mm.cs2.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.rest.dto.JugadorRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.JugadorResponse;
import py.edu.uc.lp3.mm.cs2.service.JugadorService;

@RestController
@RequestMapping(ApiPaths.JUGADORES)
@Tag(name = "Jugadores", description = "CRUD de jugadores con vida inicial 100 y chaleco opcional.")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    @Operation(operationId = "listarJugadores", summary = "Listar jugadores persistidos")
    public List<JugadorResponse> listar() {
        return jugadorService.listar().stream().map(JugadorResponse::desde).toList();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarJugador", summary = "Buscar un jugador por id")
    @ApiResponse(responseCode = "404", description = "No existe un jugador con ese id.")
    public JugadorResponse buscar(@PathVariable Long id) {
        return JugadorResponse.desde(jugadorService.buscar(id));
    }

    @PostMapping
    @Operation(operationId = "crearJugador", summary = "Crear un jugador con vida 100",
            description = "Si se indica `blindaje`, se persiste un Chaleco con esa durabilidad inicial.")
    @ApiResponse(responseCode = "201", description = "Jugador creado.")
    public ResponseEntity<JugadorResponse> crear(@Valid @RequestBody JugadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JugadorResponse.desde(jugadorService.crear(request)));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "actualizarJugador", summary = "Actualizar nombre o blindaje")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador actualizado."),
            @ApiResponse(responseCode = "404", description = "No existe un jugador con ese id.")
    })
    public JugadorResponse actualizar(@PathVariable Long id, @Valid @RequestBody JugadorRequest request) {
        return JugadorResponse.desde(jugadorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "eliminarJugador", summary = "Eliminar un jugador")
    @ApiResponse(responseCode = "204", description = "Jugador eliminado.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        jugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

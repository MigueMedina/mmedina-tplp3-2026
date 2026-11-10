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

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.rest.dto.JugadorRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.JugadorResponse;
import py.edu.uc.lp3.mm.cs2.service.JugadorService;

@RestController
@RequestMapping(ApiPaths.JUGADORES)
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public List<JugadorResponse> listar() {
        return jugadorService.listar().stream().map(JugadorResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public JugadorResponse buscar(@PathVariable Long id) {
        return JugadorResponse.desde(jugadorService.buscar(id));
    }

    @PostMapping
    public ResponseEntity<JugadorResponse> crear(@RequestBody JugadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JugadorResponse.desde(jugadorService.crear(request)));
    }

    @PutMapping("/{id}")
    public JugadorResponse actualizar(@PathVariable Long id, @RequestBody JugadorRequest request) {
        return JugadorResponse.desde(jugadorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        jugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

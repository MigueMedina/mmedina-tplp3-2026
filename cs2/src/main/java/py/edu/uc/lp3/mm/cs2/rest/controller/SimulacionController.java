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

import py.edu.uc.lp3.mm.cs2.constants.ApiPaths;
import py.edu.uc.lp3.mm.cs2.rest.dto.DisparoRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.SimulacionResponse;
import py.edu.uc.lp3.mm.cs2.service.SimulacionService;

@RestController
@RequestMapping(ApiPaths.SIMULACION)
public class SimulacionController {

    private final SimulacionService simulacionService;

    public SimulacionController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    @PostMapping("/disparar")
    public ResponseEntity<SimulacionResponse> disparar(@RequestBody DisparoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(simulacionService.disparar(request));
    }

    @GetMapping("/historial")
    public List<SimulacionResponse> historial() {
        return simulacionService.historial();
    }

    @GetMapping("/historial/jugador/{jugadorId}")
    public List<SimulacionResponse> historialDeJugador(@PathVariable Long jugadorId) {
        return simulacionService.historialDeJugador(jugadorId);
    }
}

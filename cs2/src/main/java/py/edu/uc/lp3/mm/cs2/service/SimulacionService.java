package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.ArmaDeFuego;
import py.edu.uc.lp3.mm.cs2.domain.ArmaOfensiva;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.domain.Simulacion;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.JugadorEliminadoException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.ArmaRepository;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;
import py.edu.uc.lp3.mm.cs2.repository.SimulacionRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.DisparoRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.SimulacionResponse;

@Service
public class SimulacionService {

    private final JugadorRepository jugadorRepository;
    private final ArmaRepository armaRepository;
    private final SimulacionRepository simulacionRepository;
    private final ValidadorService validadorService;

    public SimulacionService(JugadorRepository jugadorRepository,
                             ArmaRepository armaRepository,
                             SimulacionRepository simulacionRepository,
                             ValidadorService validadorService) {
        this.jugadorRepository = jugadorRepository;
        this.armaRepository = armaRepository;
        this.simulacionRepository = simulacionRepository;
        this.validadorService = validadorService;
    }

    @Transactional
    public SimulacionResponse disparar(DisparoRequest request) {
        if (request == null || request.jugadorId() == null || request.armaId() == null) {
            throw new DatosInvalidosException("Debe indicar jugadorId y armaId para simular el disparo");
        }

        Jugador jugador = jugadorRepository.findById(request.jugadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un jugador con id " + request.jugadorId()));
        Arma arma = armaRepository.findById(request.armaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un arma con id " + request.armaId()));

        if (!(arma instanceof ArmaOfensiva armaOfensiva)) {
            throw new DatosInvalidosException(
                    "El arma " + arma.getNombre() + " no inflige daño y no puede usarse para disparar");
        }

        int dano = armaOfensiva.getDano();
        validadorService.validarDano(dano);

        if (!jugador.isVivo()) {
            throw new JugadorEliminadoException(
                    "El jugador " + jugador.getNombre() + " ya está muerto y no puede recibir más disparos");
        }

        if (arma instanceof ArmaDeFuego armaDeFuego && armaDeFuego.getMunicionActual() < 1) {
            throw new MunicionInsuficienteException(
                    arma.getNombre() + " no puede disparar: cargador insuficiente (0 balas, necesita 1)");
        }

        int vidaAntes = jugador.getVida();

        arma.disparar();
        jugador.recibirDisparo(dano);
        int vidaDespues = jugador.getVida();

        armaRepository.save(arma);
        jugadorRepository.save(jugador);
        Simulacion simulacion = simulacionRepository.save(
                new Simulacion(jugador, arma, dano, vidaAntes, vidaDespues));

        return SimulacionResponse.desde(simulacion);
    }

    @Transactional(readOnly = true)
    public List<SimulacionResponse> historial() {
        return simulacionRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(SimulacionResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SimulacionResponse> historialDeJugador(Long jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) {
            throw new RecursoNoEncontradoException("No existe un jugador con id " + jugadorId);
        }
        return simulacionRepository.findByJugadorIdOrderByFechaDesc(jugadorId)
                .stream()
                .map(SimulacionResponse::desde)
                .toList();
    }
}

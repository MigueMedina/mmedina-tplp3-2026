package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.client.CompaneroClient;
import py.edu.uc.lp3.mm.cs2.client.ReglaRequest;
import py.edu.uc.lp3.mm.cs2.client.RespuestaCompanero;
import py.edu.uc.lp3.mm.cs2.client.RolCompanero;
import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.ArmaOfensiva;
import py.edu.uc.lp3.mm.cs2.domain.EscenarioCoordinado;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.ArmaRepository;
import py.edu.uc.lp3.mm.cs2.repository.EscenarioRepository;
import py.edu.uc.lp3.mm.cs2.repository.SimulacionRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.DisparoRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.SimulacionResponse;

@Service
public class EscenarioService {

    private final CompaneroClient companeroClient;
    private final SimulacionService simulacionService;
    private final ArmaRepository armaRepository;
    private final EscenarioRepository escenarioRepository;
    private final SimulacionRepository simulacionRepository;

    public EscenarioService(CompaneroClient companeroClient,
                            SimulacionService simulacionService,
                            ArmaRepository armaRepository,
                            EscenarioRepository escenarioRepository,
                            SimulacionRepository simulacionRepository) {
        this.companeroClient = companeroClient;
        this.simulacionService = simulacionService;
        this.armaRepository = armaRepository;
        this.escenarioRepository = escenarioRepository;
        this.simulacionRepository = simulacionRepository;
    }

    @Transactional
    public EscenarioResponse ejecutar(EscenarioRequest request) {
        if (request == null || request.jugadorId() == null || request.armaId() == null) {
            throw new DatosInvalidosException("Debe indicar jugadorId y armaId para el escenario");
        }

        Arma arma = armaRepository.findById(request.armaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un arma con id " + request.armaId()));
        if (!(arma instanceof ArmaOfensiva armaOfensiva)) {
            throw new DatosInvalidosException(
                    "El arma " + arma.getNombre() + " no inflige daño y no puede usarse en el escenario");
        }

        ReglaRequest regla = new ReglaRequest(request.jugadorId(), request.armaId(), armaOfensiva.getDano());
        RespuestaCompanero aprobacionA2 = companeroClient.validar(RolCompanero.A2, regla);
        RespuestaCompanero aprobacionA3 = companeroClient.validar(RolCompanero.A3, regla);

        SimulacionResponse simulacion = simulacionService.disparar(
                new DisparoRequest(request.jugadorId(), request.armaId()));
        EscenarioCoordinado entidad = escenarioRepository.save(
                new EscenarioCoordinado(simulacion.id(), aprobacionA2, aprobacionA3));

        return EscenarioResponse.desde(entidad, simulacion);
    }

    @Transactional(readOnly = true)
    public List<EscenarioResponse> listar() {
        return escenarioRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(this::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public EscenarioResponse buscar(Long id) {
        EscenarioCoordinado entidad = escenarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un escenario coordinado con id " + id));
        return desde(entidad);
    }

    private EscenarioResponse desde(EscenarioCoordinado entidad) {
        SimulacionResponse simulacion = simulacionRepository.findById(entidad.getSimulacionId())
                .map(SimulacionResponse::desde)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la simulación " + entidad.getSimulacionId() + " del escenario"));
        return EscenarioResponse.desde(entidad, simulacion);
    }
}

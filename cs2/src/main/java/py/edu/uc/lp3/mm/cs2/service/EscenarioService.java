package py.edu.uc.lp3.mm.cs2.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import py.edu.uc.lp3.mm.cs2.client.CompaneroClient;
import py.edu.uc.lp3.mm.cs2.client.ReglaRequest;
import py.edu.uc.lp3.mm.cs2.client.RespuestaCompanero;
import py.edu.uc.lp3.mm.cs2.client.RolCompanero;
import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.ArmaDeFuego;
import py.edu.uc.lp3.mm.cs2.domain.ArmaOfensiva;
import py.edu.uc.lp3.mm.cs2.domain.EscenarioCoordinado;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.domain.Simulacion;
import py.edu.uc.lp3.mm.cs2.exception.CompanneroRechazoException;
import py.edu.uc.lp3.mm.cs2.exception.ConflictoDeEstadoException;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.ArmaRepository;
import py.edu.uc.lp3.mm.cs2.repository.EscenarioRepository;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.DisparoRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.EscenarioResponse;

@Service
public class EscenarioService {

    private static final String TIRADOR = "A1-MigueMedina";
    private static final String EQUIPO_TIRADOR_POR_DEFECTO = "TERRORISTAS";
    private static final String EQUIPO_OBJETIVO_POR_DEFECTO = "ANTITERRORISTAS";

    private final CompaneroClient companeroClient;
    private final SimulacionService simulacionService;
    private final ArmaRepository armaRepository;
    private final JugadorRepository jugadorRepository;
    private final EscenarioRepository escenarioRepository;
    private final TransactionTemplate transacciones;

    public EscenarioService(CompaneroClient companeroClient,
                            SimulacionService simulacionService,
                            ArmaRepository armaRepository,
                            JugadorRepository jugadorRepository,
                            EscenarioRepository escenarioRepository,
                            PlatformTransactionManager transactionManager) {
        this.companeroClient = companeroClient;
        this.simulacionService = simulacionService;
        this.armaRepository = armaRepository;
        this.jugadorRepository = jugadorRepository;
        this.escenarioRepository = escenarioRepository;
        this.transacciones = new TransactionTemplate(transactionManager);
    }

    public EscenarioResponse ejecutar(EscenarioRequest request) {
        if (request == null || request.jugadorId() == null || request.armaId() == null) {
            throw new DatosInvalidosException("Debe indicar jugadorId y armaId para el escenario");
        }

        String idempotencyKey = clave(request.idempotencyKey());
        if (idempotencyKey != null) {
            Optional<EscenarioCoordinado> previo = escenarioRepository.findByIdempotencyKey(idempotencyKey);
            if (previo.isPresent()) {
                return EscenarioResponse.desde(previo.get());
            }
        }

        DatosValidados datos = cargarDatos(request);
        RespuestaCompanero aprobacionA2 = companeroClient.validar(RolCompanero.A2, cuerpoA2(datos));
        exigirAprobacion(RolCompanero.A2, aprobacionA2);
        RespuestaCompanero aprobacionA3 = companeroClient.validar(
                RolCompanero.A3, cuerpoA3(datos, request));
        exigirAprobacion(RolCompanero.A3, aprobacionA3);

        try {
            return transacciones.execute(status ->
                    registrar(idempotencyKey, datos, aprobacionA2, aprobacionA3));
        } catch (DataIntegrityViolationException excepcion) {
            if (idempotencyKey == null) {
                throw excepcion;
            }
            return escenarioRepository.findByIdempotencyKey(idempotencyKey)
                    .map(EscenarioResponse::desde)
                    .orElseThrow(() -> excepcion);
        }
    }

    @Transactional(readOnly = true)
    public List<EscenarioResponse> listar() {
        return escenarioRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(EscenarioResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public EscenarioResponse buscar(Long id) {
        EscenarioCoordinado entidad = escenarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un escenario coordinado con id " + id));
        return EscenarioResponse.desde(entidad);
    }

    private DatosValidados cargarDatos(EscenarioRequest request) {
        Jugador objetivo = jugadorRepository.findById(request.jugadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un jugador con id " + request.jugadorId()));
        Arma arma = armaRepository.findById(request.armaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un arma con id " + request.armaId()));
        if (!(arma instanceof ArmaOfensiva armaOfensiva)) {
            throw new DatosInvalidosException(
                    "El arma " + arma.getNombre() + " no inflige daño y no puede usarse en el escenario");
        }
        int municion = arma instanceof ArmaDeFuego armaDeFuego
                ? armaDeFuego.getMunicionActual()
                : 1;
        return new DatosValidados(request.jugadorId(), request.armaId(), objetivo.getNombre(),
                objetivo.getVida(), objetivo.getBlindaje(), armaOfensiva.getDano(), municion);
    }

    private EscenarioResponse registrar(String idempotencyKey, DatosValidados datos,
                                        RespuestaCompanero aprobacionA2, RespuestaCompanero aprobacionA3) {
        Jugador objetivo = jugadorRepository.findById(datos.jugadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un jugador con id " + datos.jugadorId()));
        Arma arma = armaRepository.findById(datos.armaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un arma con id " + datos.armaId()));
        if (!estadoCoincide(datos, objetivo, arma)) {
            throw new ConflictoDeEstadoException(
                    "El jugador o el arma cambiaron durante la validación; reintente el escenario");
        }

        Simulacion simulacion = simulacionService.dispararEntidad(
                new DisparoRequest(datos.jugadorId(), datos.armaId()));
        EscenarioCoordinado entidad = escenarioRepository.save(
                new EscenarioCoordinado(idempotencyKey, simulacion, aprobacionA2, aprobacionA3));
        return EscenarioResponse.desde(entidad);
    }

    private boolean estadoCoincide(DatosValidados datos, Jugador objetivo, Arma arma) {
        if (objetivo.getVida() != datos.vida() || objetivo.getBlindaje() != datos.blindaje()) {
            return false;
        }
        if (!(arma instanceof ArmaOfensiva armaOfensiva) || armaOfensiva.getDano() != datos.dano()) {
            return false;
        }
        int municion = arma instanceof ArmaDeFuego armaDeFuego
                ? armaDeFuego.getMunicionActual()
                : 1;
        return municion == datos.municion();
    }

    private void exigirAprobacion(RolCompanero rol, RespuestaCompanero respuesta) {
        if (!respuesta.aprobo()) {
            throw new CompanneroRechazoException(rol, respuesta.status(), respuesta.cuerpo());
        }
    }

    private ReglaRequest cuerpoA2(DatosValidados datos) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("cargador", datos.municion());
        valores.put("balasPorDisparo", 1);
        valores.put("disparos", 1);
        return new ReglaRequest(valores);
    }

    private ReglaRequest cuerpoA3(DatosValidados datos, EscenarioRequest request) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("tirador", TIRADOR);
        valores.put("equipoTirador", valor(request.equipoTirador(), EQUIPO_TIRADOR_POR_DEFECTO));
        valores.put("objetivo", datos.nombreObjetivo());
        valores.put("equipoObjetivo", valor(request.equipoObjetivo(), EQUIPO_OBJETIVO_POR_DEFECTO));
        valores.put("vidaObjetivo", datos.vida());
        valores.put("blindajeObjetivo", datos.blindaje());
        valores.put("danio", datos.dano());
        return new ReglaRequest(valores);
    }

    private String valor(String valor, String defecto) {
        return valor == null || valor.isBlank() ? defecto : valor.trim().toUpperCase();
    }

    private String clave(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private record DatosValidados(Long jugadorId, Integer armaId, String nombreObjetivo,
                                  int vida, int blindaje, int dano, int municion) {
    }
}

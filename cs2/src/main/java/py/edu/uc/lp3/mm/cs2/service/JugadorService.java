package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Chaleco;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.JugadorRequest;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Transactional(readOnly = true)
    public List<Jugador> listar() {
        return jugadorRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Jugador buscar(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un jugador con id " + id));
    }

    @Transactional
    public Jugador crear(JugadorRequest request) {
        if (request == null) {
            throw new DatosInvalidosException("Debe indicar el nombre del jugador");
        }
        try {
            Chaleco chaleco = crearChaleco(request.blindaje());
            return jugadorRepository.save(new Jugador(request.nombre(), Jugador.VIDA_INICIAL, chaleco));
        } catch (IllegalArgumentException excepcion) {
            throw new DatosInvalidosException(excepcion.getMessage(), excepcion);
        }
    }

    @Transactional
    public Jugador actualizar(Long id, JugadorRequest request) {
        Jugador jugador = buscar(id);
        if (request.nombre() != null) {
            jugador.setNombre(request.nombre());
        }
        if (request.blindaje() != null) {
            jugador.equiparChaleco(crearChaleco(request.blindaje()));
        }
        return jugadorRepository.save(jugador);
    }

    private Chaleco crearChaleco(Integer blindaje) {
        if (blindaje == null || blindaje == 0) {
            return null;
        }
        return new Chaleco(blindaje);
    }

    @Transactional
    public void eliminar(Long id) {
        Jugador jugador = buscar(id);
        jugadorRepository.delete(jugador);
    }
}

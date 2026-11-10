package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.ArmaRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;

@Service
public class ArmaService {

    private final ArmaRepository armaRepository;

    public ArmaService(ArmaRepository armaRepository) {
        this.armaRepository = armaRepository;
    }

    @Transactional(readOnly = true)
    public List<Arma> listar() {
        return armaRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public List<Arma> buscarPorNombre(String nombre) {
        return armaRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(nombre);
    }

    @Transactional(readOnly = true)
    public Arma buscar(Integer id) {
        return armaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un arma con id " + id));
    }

    @Transactional
    public Arma crear(ArmaRequest request) {
        try {
            return armaRepository.save(ArmaMapper.crear(request));
        } catch (IllegalArgumentException excepcion) {
            throw new DatosInvalidosException(excepcion.getMessage(), excepcion);
        }
    }

    @Transactional
    public Arma actualizar(Integer id, ArmaRequest request) {
        Arma arma = buscar(id);
        try {
            ArmaMapper.aplicar(arma, request);
            return armaRepository.save(arma);
        } catch (IllegalArgumentException excepcion) {
            throw new DatosInvalidosException(excepcion.getMessage(), excepcion);
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Arma arma = buscar(id);
        armaRepository.delete(arma);
    }
}

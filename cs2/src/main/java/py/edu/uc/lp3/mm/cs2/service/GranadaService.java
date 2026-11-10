package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.GranadaRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;

@Service
public class GranadaService {

    private final GranadaRepository granadaRepository;

    public GranadaService(GranadaRepository granadaRepository) {
        this.granadaRepository = granadaRepository;
    }

    @Transactional(readOnly = true)
    public List<Granada> listar() {
        return granadaRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Granada buscar(Integer id) {
        return granadaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una granada con id " + id));
    }

    @Transactional
    public Granada crear(ArmaRequest request) {
        try {
            return granadaRepository.save(ArmaMapper.crearGranada(request));
        } catch (IllegalArgumentException excepcion) {
            throw new DatosInvalidosException(excepcion.getMessage(), excepcion);
        }
    }

    @Transactional
    public Granada actualizar(Integer id, ArmaRequest request) {
        Granada granada = buscar(id);
        try {
            ArmaMapper.aplicar(granada, request);
            return granadaRepository.save(granada);
        } catch (IllegalArgumentException excepcion) {
            throw new DatosInvalidosException(excepcion.getMessage(), excepcion);
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Granada granada = buscar(id);
        granadaRepository.delete(granada);
    }
}

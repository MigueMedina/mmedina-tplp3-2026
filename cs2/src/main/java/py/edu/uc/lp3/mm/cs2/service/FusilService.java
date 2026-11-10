package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.FusilRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;

@Service
public class FusilService {

    private final FusilRepository fusilRepository;

    public FusilService(FusilRepository fusilRepository) {
        this.fusilRepository = fusilRepository;
    }

    @Transactional(readOnly = true)
    public List<Fusil> listar() {
        return fusilRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Fusil buscar(Integer id) {
        return fusilRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un fusil con id " + id));
    }

    @Transactional
    public Fusil crear(ArmaRequest request) {
        try {
            return fusilRepository.save(ArmaMapper.crearFusil(request));
        } catch (IllegalArgumentException excepcion) {
            throw new DatosInvalidosException(excepcion.getMessage(), excepcion);
        }
    }

    @Transactional
    public Fusil actualizar(Integer id, ArmaRequest request) {
        Fusil fusil = buscar(id);
        try {
            ArmaMapper.aplicar(fusil, request);
            return fusilRepository.save(fusil);
        } catch (IllegalArgumentException excepcion) {
            throw new DatosInvalidosException(excepcion.getMessage(), excepcion);
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Fusil fusil = buscar(id);
        fusilRepository.delete(fusil);
    }
}

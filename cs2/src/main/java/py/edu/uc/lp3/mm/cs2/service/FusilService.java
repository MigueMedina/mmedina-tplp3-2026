package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.FusilRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;

@Service
public class FusilService {

    private final FusilRepository fusilRepository;
    private final ValidadorService validadorService;

    public FusilService(FusilRepository fusilRepository, ValidadorService validadorService) {
        this.fusilRepository = fusilRepository;
        this.validadorService = validadorService;
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
        Fusil fusil = ArmaMapper.crearFusil(request);
        validadorService.validarArma(fusil);
        return fusilRepository.save(fusil);
    }

    @Transactional
    public Fusil actualizar(Integer id, ArmaRequest request) {
        Fusil fusil = buscar(id);
        ArmaMapper.aplicar(fusil, request);
        validadorService.validarArma(fusil);
        return fusilRepository.save(fusil);
    }

    @Transactional
    public void eliminar(Integer id) {
        Fusil fusil = buscar(id);
        fusilRepository.delete(fusil);
    }
}

package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.GranadaRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;

@Service
public class GranadaService {

    private final GranadaRepository granadaRepository;
    private final ValidadorService validadorService;

    public GranadaService(GranadaRepository granadaRepository, ValidadorService validadorService) {
        this.granadaRepository = granadaRepository;
        this.validadorService = validadorService;
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
        Granada granada = ArmaMapper.crearGranada(request);
        validadorService.validarArma(granada);
        return granadaRepository.save(granada);
    }

    @Transactional
    public Granada actualizar(Integer id, ArmaRequest request) {
        Granada granada = buscar(id);
        ArmaMapper.aplicar(granada, request);
        validadorService.validarArma(granada);
        return granadaRepository.save(granada);
    }

    @Transactional
    public void eliminar(Integer id) {
        Granada granada = buscar(id);
        granadaRepository.delete(granada);
    }
}

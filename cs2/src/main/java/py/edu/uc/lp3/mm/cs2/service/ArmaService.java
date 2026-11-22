package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.repository.ArmaRepository;
import py.edu.uc.lp3.mm.cs2.rest.dto.ArmaRequest;

@Service
public class ArmaService {

    private final ArmaRepository armaRepository;
    private final ValidadorService validadorService;

    public ArmaService(ArmaRepository armaRepository, ValidadorService validadorService) {
        this.armaRepository = armaRepository;
        this.validadorService = validadorService;
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
        Arma arma = ArmaMapper.crear(request);
        validadorService.validarArma(arma);
        return armaRepository.save(arma);
    }

    @Transactional
    public Arma actualizar(Integer id, ArmaRequest request) {
        Arma arma = buscar(id);
        ArmaMapper.aplicar(arma, request);
        validadorService.validarArma(arma);
        return armaRepository.save(arma);
    }

    @Transactional
    public void eliminar(Integer id) {
        Arma arma = buscar(id);
        armaRepository.delete(arma);
    }

    public String recargar(Arma arma) {
        validadorService.validarRecarga(arma);
        return arma.recargar();
    }
}

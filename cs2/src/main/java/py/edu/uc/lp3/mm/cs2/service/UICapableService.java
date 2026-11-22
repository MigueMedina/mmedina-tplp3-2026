package py.edu.uc.lp3.mm.cs2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.mm.cs2.domain.UICapable;
import py.edu.uc.lp3.mm.cs2.repository.ArmaRepository;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;

@Service
public class UICapableService {

    private final ArmaRepository armaRepository;
    private final JugadorRepository jugadorRepository;

    public UICapableService(ArmaRepository armaRepository, JugadorRepository jugadorRepository) {
        this.armaRepository = armaRepository;
        this.jugadorRepository = jugadorRepository;
    }

    @Transactional(readOnly = true)
    public List<UICapable> listar() {
        List<UICapable> elementos = new ArrayList<>();
        armaRepository.findAllByOrderByIdAsc().stream()
                .filter(UICapable.class::isInstance)
                .forEach(arma -> elementos.add((UICapable) arma));
        jugadorRepository.findAllByOrderByIdAsc().stream()
                .filter(UICapable.class::isInstance)
                .forEach(jugador -> elementos.add((UICapable) jugador));
        return elementos;
    }
}

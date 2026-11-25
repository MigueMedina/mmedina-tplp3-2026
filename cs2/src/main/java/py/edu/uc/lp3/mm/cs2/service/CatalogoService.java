package py.edu.uc.lp3.mm.cs2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.mm.cs2.constants.CatalogoArma;
import py.edu.uc.lp3.mm.cs2.rest.dto.CatalogoArmaResponse;

@Service
public class CatalogoService {

    public List<CatalogoArmaResponse> listar() {
        return CatalogoArma.todos().stream()
                .map(CatalogoArmaResponse::desde)
                .toList();
    }
}

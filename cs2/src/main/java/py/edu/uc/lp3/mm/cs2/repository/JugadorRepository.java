package py.edu.uc.lp3.mm.cs2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.mm.cs2.domain.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    List<Jugador> findAllByOrderByIdAsc();

    Optional<Jugador> findByNombreIgnoreCase(String nombre);
}

package py.edu.uc.lp3.mm.cs2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.mm.cs2.domain.Simulacion;

@Repository
public interface SimulacionRepository extends JpaRepository<Simulacion, Long> {

    List<Simulacion> findAllByOrderByFechaDesc();

    List<Simulacion> findByJugadorIdOrderByFechaDesc(Long jugadorId);
}

package py.edu.uc.lp3.mm.cs2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.mm.cs2.domain.EscenarioCoordinado;

@Repository
public interface EscenarioRepository extends JpaRepository<EscenarioCoordinado, Long> {

    List<EscenarioCoordinado> findAllByOrderByFechaDesc();

    Optional<EscenarioCoordinado> findByIdempotencyKey(String idempotencyKey);
}

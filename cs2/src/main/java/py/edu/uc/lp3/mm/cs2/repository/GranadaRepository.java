package py.edu.uc.lp3.mm.cs2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.mm.cs2.domain.Granada;

@Repository
public interface GranadaRepository extends JpaRepository<Granada, Integer> {

    List<Granada> findAllByOrderByIdAsc();
}

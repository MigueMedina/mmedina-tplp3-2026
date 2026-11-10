package py.edu.uc.lp3.mm.cs2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.mm.cs2.domain.Fusil;

@Repository
public interface FusilRepository extends JpaRepository<Fusil, Integer> {

    List<Fusil> findAllByOrderByIdAsc();
}

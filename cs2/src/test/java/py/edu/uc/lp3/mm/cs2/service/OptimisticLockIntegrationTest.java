package py.edu.uc.lp3.mm.cs2.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.repository.JugadorRepository;

@SpringBootTest
class OptimisticLockIntegrationTest {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void unaEscrituraConcurrenteSobreElMismoJugadorSeRechaza() {
        Long id = jugadorRepository.saveAndFlush(new Jugador("Concurrente")).getId();
        TransactionTemplate transacciones = new TransactionTemplate(transactionManager);

        Jugador primero = transacciones.execute(status -> jugadorRepository.findById(id).orElseThrow());
        Jugador segundo = transacciones.execute(status -> jugadorRepository.findById(id).orElseThrow());
        primero.setNombre("Primero");
        segundo.setNombre("Segundo");

        try {
            transacciones.execute(status -> jugadorRepository.saveAndFlush(primero));
            assertThrows(ObjectOptimisticLockingFailureException.class,
                    () -> transacciones.execute(status -> jugadorRepository.saveAndFlush(segundo)));
        } finally {
            jugadorRepository.deleteById(id);
            jugadorRepository.flush();
        }
    }
}

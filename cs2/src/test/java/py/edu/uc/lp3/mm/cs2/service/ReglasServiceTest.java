package py.edu.uc.lp3.mm.cs2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.JugadorEliminadoException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaConsultaRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaJugadorVivoResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaMunicionResponse;

class ReglasServiceTest {

    private final ReglasService service = new ReglasService();

    @Test
    void laMuniciónSuficienteSeAceptaYSinBalaSeRechaza() {
        ReglaMunicionResponse aceptada = service.validarMunicion(new ReglaConsultaRequest(1, 30, null, null));
        assertTrue(aceptada.permitido());
        assertEquals(1, aceptada.carga());

        assertThrows(MunicionInsuficienteException.class,
                () -> service.validarMunicion(new ReglaConsultaRequest(0, 30, null, null)));
        assertThrows(DatosInvalidosException.class,
                () -> service.validarMunicion(new ReglaConsultaRequest(-1, 30, null, null)));
        assertThrows(DatosInvalidosException.class,
                () -> service.validarMunicion(new ReglaConsultaRequest(1, 0, null, null)));
    }

    @Test
    void elJugadorVivoSeAceptaYElEliminadoSeRechaza() {
        ReglaJugadorVivoResponse aceptada =
                service.validarJugadorVivo(new ReglaConsultaRequest(null, null, 100, 0));
        assertTrue(aceptada.permitido());
        assertEquals(0, aceptada.blindaje());

        assertThrows(JugadorEliminadoException.class,
                () -> service.validarJugadorVivo(new ReglaConsultaRequest(null, null, 0, 0)));
        assertThrows(DatosInvalidosException.class,
                () -> service.validarJugadorVivo(new ReglaConsultaRequest(null, null, 150, 0)));
        assertThrows(DatosInvalidosException.class,
                () -> service.validarJugadorVivo(new ReglaConsultaRequest(null, null, 50, 150)));
    }
}

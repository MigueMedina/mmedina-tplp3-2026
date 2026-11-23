package py.edu.uc.lp3.mm.cs2.service;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.JugadorEliminadoException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaConsultaRequest;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaJugadorVivoResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.ReglaMunicionResponse;

@Service
public class ReglasService {

    public static final String REGLA_MUNICION =
            "Un arma de fuego dispara si el cargador tiene al menos una bala; sin balas no dispara.";
    public static final String REGLA_VIDA =
            "Un jugador con vida mayor que 0 sigue en juego; con vida 0 no recibe más disparos.";

    public ReglaMunicionResponse validarMunicion(ReglaConsultaRequest request) {
        if (request == null || request.carga() == null || request.dano() == null) {
            throw new DatosInvalidosException("Debe indicar carga y dano para validar la munición");
        }
        if (request.carga() < 0) {
            throw new DatosInvalidosException("La carga del cargador no puede ser negativa");
        }
        if (request.dano() <= 0) {
            throw new DatosInvalidosException("El daño debe ser mayor que cero");
        }
        if (request.carga() < 1) {
            throw new MunicionInsuficienteException(
                    "No hay munición suficiente: cargador en 0 y se necesita 1 bala");
        }
        return new ReglaMunicionResponse(true, REGLA_MUNICION,
                "Hay munición suficiente para un disparo", request.carga(), request.dano());
    }

    public ReglaJugadorVivoResponse validarJugadorVivo(ReglaConsultaRequest request) {
        if (request == null || request.vida() == null) {
            throw new DatosInvalidosException("Debe indicar la vida del jugador");
        }
        if (request.vida() < 0 || request.vida() > 100) {
            throw new DatosInvalidosException("La vida del jugador debe estar entre 0 y 100");
        }
        int blindaje = request.blindaje() == null ? 0 : request.blindaje();
        if (blindaje < 0 || blindaje > 100) {
            throw new DatosInvalidosException("El blindaje debe estar entre 0 y 100");
        }
        if (request.vida() == 0) {
            throw new JugadorEliminadoException("El jugador ya fue eliminado: vida 0");
        }
        return new ReglaJugadorVivoResponse(true, REGLA_VIDA,
                "El jugador sigue vivo y puede recibir el disparo", request.vida(), blindaje);
    }
}

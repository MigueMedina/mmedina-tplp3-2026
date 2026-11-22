package py.edu.uc.lp3.mm.cs2.service;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.mm.cs2.domain.AWP;
import py.edu.uc.lp3.mm.cs2.domain.Arma;
import py.edu.uc.lp3.mm.cs2.domain.Chaleco;
import py.edu.uc.lp3.mm.cs2.domain.ArmaDeFuego;
import py.edu.uc.lp3.mm.cs2.domain.Arrojadiza;
import py.edu.uc.lp3.mm.cs2.domain.Escopeta;
import py.edu.uc.lp3.mm.cs2.domain.Flash;
import py.edu.uc.lp3.mm.cs2.domain.Fusil;
import py.edu.uc.lp3.mm.cs2.domain.Granada;
import py.edu.uc.lp3.mm.cs2.domain.Jugador;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;

@Service
public class ValidadorService {

    public void validarArma(Arma arma) {
        validarNoVacio(arma.getNombre(), "El nombre no puede estar vacío");
        validarNoNegativo(arma.getPrecio(), "El precio debe ser no negativo");
        validarNoNegativo(arma.getId(), "El id debe ser no negativo");

        if (arma instanceof ArmaDeFuego armaDeFuego) {
            validarNoNegativo(armaDeFuego.getDano(), "El daño debe ser no negativo");
            if (armaDeFuego.getPrecision() < 0 || armaDeFuego.getPrecision() > 100) {
                throw new DatosInvalidosException("La precisión debe estar entre 0 y 100");
            }
            validarNoNegativo(armaDeFuego.getRecarga(), "La recarga debe ser no negativa");
            validarNoNegativo(armaDeFuego.getVelocidad(), "La velocidad debe ser no negativa");
            validarNoNegativo(armaDeFuego.getCapacidadCargador(), "La capacidad del cargador debe ser no negativa");
            validarNoNegativo(armaDeFuego.getCargadoresRestantes(), "Los cargadores restantes deben ser no negativos");
            if (armaDeFuego.getMunicionActual() < 0
                    || armaDeFuego.getMunicionActual() > armaDeFuego.getCapacidadCargador()) {
                throw new DatosInvalidosException(
                        "La munición debe estar entre 0 y " + armaDeFuego.getCapacidadCargador());
            }
        }

        if (arma instanceof Arrojadiza arrojadiza) {
            if (arrojadiza.getTipo() == null) {
                throw new DatosInvalidosException("El tipo no puede ser nulo");
            }
            validarNoNegativo(arrojadiza.getRadio(), "El radio debe ser no negativo");
            validarNoNegativo(arrojadiza.getDistancia(), "La distancia debe ser no negativa");
            validarNoNegativo(arrojadiza.getDuracion(), "La duración debe ser no negativa");
            validarNoNegativo(arrojadiza.getCooldown(), "El cooldown debe ser no negativo");
        }

        if (arma instanceof Fusil fusil) {
            validarNoNegativo(fusil.getMira(), "La mira debe ser no negativa");
            validarNoNegativo(fusil.getRetroceso(), "El retroceso debe ser no negativo");
        }

        if (arma instanceof AWP awp) {
            validarNoNegativo(awp.getMira(), "La mira debe ser no negativa");
            validarNoNegativo(awp.getRetroceso(), "El retroceso debe ser no negativo");
        }

        if (arma instanceof Escopeta escopeta) {
            validarNoNegativo(escopeta.getPerdigon(), "La cantidad de perdigones debe ser no negativa");
            validarNoNegativo(escopeta.getDispersion(), "La dispersión debe ser no negativa");
        }

        if (arma instanceof Granada granada) {
            validarNoNegativo(granada.getDano(), "El daño debe ser no negativo");
        }

        if (arma instanceof Flash flash) {
            validarNoNegativo(flash.getIntensidad(), "La intensidad debe ser no negativa");
            validarNoNegativo(flash.getDuracionCeguera(), "La duración de ceguera debe ser no negativa");
        }
    }

    public void validarJugador(Jugador jugador) {
        validarNoVacio(jugador.getNombre(), "El nombre del jugador no puede estar vacío");
        if (jugador.getVida() < 0 || jugador.getVida() > Jugador.VIDA_INICIAL) {
            throw new DatosInvalidosException(
                    "La vida del jugador debe estar entre 0 y " + Jugador.VIDA_INICIAL);
        }
    }

    public void validarChaleco(int durabilidad) {
        if (durabilidad < 0 || durabilidad > Chaleco.DURABILIDAD_MAXIMA) {
            throw new DatosInvalidosException(
                    "La durabilidad del chaleco debe estar entre 0 y " + Chaleco.DURABILIDAD_MAXIMA);
        }
    }

    public void validarDano(int dano) {
        if (dano <= 0) {
            throw new DatosInvalidosException("El arma debe infligir un daño mayor que cero");
        }
    }

    public void validarRecarga(Arma arma) {
        if (arma instanceof ArmaDeFuego armaDeFuego
                && armaDeFuego.getMunicionActual() < armaDeFuego.getCapacidadCargador()
                && armaDeFuego.getCargadoresRestantes() <= 0) {
            throw new MunicionInsuficienteException(
                    armaDeFuego.getNombre() + " no tiene cargadores de repuesto");
        }
    }

    private void validarNoVacio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new DatosInvalidosException(mensaje);
        }
    }

    private void validarNoNegativo(double valor, String mensaje) {
        if (valor < 0) {
            throw new DatosInvalidosException(mensaje);
        }
    }
}

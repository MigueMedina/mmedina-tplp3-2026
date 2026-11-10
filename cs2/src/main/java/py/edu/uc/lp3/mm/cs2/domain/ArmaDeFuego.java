package py.edu.uc.lp3.mm.cs2.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;

@Entity
@DiscriminatorValue("FUEGO")
public abstract class ArmaDeFuego extends Arma implements ArmaOfensiva {
    public static final int CARGADORES_INICIALES = 3;

    private int dano;
    private int precision;
    private double recarga;
    private double velocidad;
    private int municionActual;
    private int capacidadCargador;
    private int cargadoresRestantes = CARGADORES_INICIALES;

    protected ArmaDeFuego() {
    }

    public ArmaDeFuego(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                       int capacidadCargador) {
        super(nombre, id, precio);
        setDano(dano);
        setPrecision(precision);
        setRecarga(recarga);
        setVelocidad(velocidad);
        setCapacidadCargador(capacidadCargador);
        this.municionActual = capacidadCargador;
    }

    // Getters y Setters (con invariantes)
    public int getDano() { return dano; }
    public void setDano(int dano) {
        if (dano < 0)
            throw new IllegalArgumentException("El daño debe ser no negativo");
        this.dano = dano;
    }

    public int getPrecision() { return precision; }
    public void setPrecision(int precision) {
        if (precision < 0 || precision > 100)
            throw new IllegalArgumentException("La precisión debe estar entre 0 y 100");
        this.precision = precision;
    }

    public double getRecarga() { return recarga; }
    public void setRecarga(double recarga) {
        if (recarga < 0)
            throw new IllegalArgumentException("La recarga debe ser no negativa");
        this.recarga = recarga;
    }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) {
        if (velocidad < 0)
            throw new IllegalArgumentException("La velocidad debe ser no negativa");
        this.velocidad = velocidad;
    }

    public int getMunicionActual() { return municionActual; }
    public void setMunicionActual(int municionActual) {
        if (municionActual < 0 || municionActual > capacidadCargador)
            throw new IllegalArgumentException("La munición debe estar entre 0 y " + capacidadCargador);
        this.municionActual = municionActual;
    }

    public int getCapacidadCargador() { return capacidadCargador; }
    public void setCapacidadCargador(int capacidadCargador) {
        if (capacidadCargador < 0)
            throw new IllegalArgumentException("La capacidad del cargador debe ser no negativa");
        this.capacidadCargador = capacidadCargador;
        if (this.municionActual > this.capacidadCargador) {
            this.municionActual = this.capacidadCargador;
        }
    }

    public int getCargadoresRestantes() { return cargadoresRestantes; }
    public void setCargadoresRestantes(int cargadoresRestantes) {
        if (cargadoresRestantes < 0)
            throw new IllegalArgumentException("Los cargadores restantes deben ser no negativos");
        this.cargadoresRestantes = cargadoresRestantes;
    }

    // UICapable: la velocidad del arma representa su velocidad de disparo
    public double getVelocidadDisparo() {
        return velocidad;
    }

    protected String dispararConMunicion(String mensaje) {
        consumirMunicion(1);
        return mensaje + " Quedan " + municionActual + "/" + capacidadCargador + " balas.";
    }

    protected void consumirMunicion(int balas) {
        if (balas <= 0) {
            throw new DatosInvalidosException("La cantidad de balas a consumir debe ser positiva");
        }
        if (municionActual < balas) {
            throw new MunicionInsuficienteException(
                    getNombre() + " no puede disparar: cargador insuficiente ("
                            + municionActual + " balas, necesita " + balas + ")");
        }
        municionActual -= balas;
    }

    @Override
    public String recargar() {
        if (municionActual == capacidadCargador) {
            return getNombre() + " ya tiene el cargador lleno";
        }
        return recargar(capacidadCargador);
    }

    // Sobrecarga de recargar(): recarga parcial hasta completar el cargador.
    // Toda recarga consume un cargador de repuesto, aunque la cantidad pedida
    // no alcance a llenar el cargador.
    public String recargar(int cantidad) {
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad a recargar debe ser positiva");
        if (municionActual == capacidadCargador) {
            return getNombre() + " ya tiene el cargador lleno";
        }
        if (cargadoresRestantes <= 0) {
            throw new MunicionInsuficienteException(
                    getNombre() + " no tiene cargadores de repuesto");
        }
        cargadoresRestantes--;
        this.municionActual = Math.min(capacidadCargador, municionActual + cantidad);
        return "Munición recargada a " + this.municionActual + " balas. Cargadores restantes: "
                + cargadoresRestantes;
    }
}

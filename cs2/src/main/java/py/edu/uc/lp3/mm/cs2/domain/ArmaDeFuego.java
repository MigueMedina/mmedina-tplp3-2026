package py.edu.uc.lp3.mm.cs2.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

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

    public int getDano() { return dano; }
    public void setDano(int dano) { this.dano = dano; }

    public int getPrecision() { return precision; }
    public void setPrecision(int precision) { this.precision = precision; }

    public double getRecarga() { return recarga; }
    public void setRecarga(double recarga) { this.recarga = recarga; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }

    public int getMunicionActual() { return municionActual; }
    private void setMunicionActual(int municionActual) { this.municionActual = municionActual; }

    public int getCapacidadCargador() { return capacidadCargador; }
    public void setCapacidadCargador(int capacidadCargador) {
        this.capacidadCargador = capacidadCargador;
        this.municionActual = Math.min(this.municionActual, capacidadCargador);
    }

    public int getCargadoresRestantes() { return cargadoresRestantes; }
    public void setCargadoresRestantes(int cargadoresRestantes) { this.cargadoresRestantes = cargadoresRestantes; }

    public double getVelocidadDisparo() {
        return velocidad;
    }

    protected String dispararConMunicion(String mensaje) {
        consumirMunicion(1);
        return mensaje + " Quedan " + municionActual + "/" + capacidadCargador + " balas.";
    }

    private void consumirMunicion(int balas) {
        this.municionActual = Math.max(0, this.municionActual - balas);
    }

    @Override
    public String recargar() {
        if (municionActual == capacidadCargador) {
            return getNombre() + " ya tiene el cargador lleno";
        }
        return recargar(capacidadCargador);
    }

    public String recargar(int cantidad) {
        if (municionActual == capacidadCargador) {
            return getNombre() + " ya tiene el cargador lleno";
        }
        if (cargadoresRestantes <= 0) {
            return getNombre() + " no tiene cargadores de repuesto";
        }
        cargadoresRestantes--;
        setMunicionActual(Math.min(capacidadCargador, municionActual + Math.max(0, cantidad)));
        return "Munición recargada a " + this.municionActual + " balas. Cargadores restantes: "
                + cargadoresRestantes;
    }
}

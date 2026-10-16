package py.edu.uc.lp3.mm.cs2.domain;

public abstract class ArmaDeFuego extends Arma {
    private int dano;
    private int precision;
    private double recarga;
    private double velocidad;
    private int municionActual;
    private int capacidadCargador;

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

    // UICapable: la velocidad del arma representa su velocidad de disparo
    public double getVelocidadDisparo() {
        return velocidad;
    }

    @Override
    public String recargar() {
        this.municionActual = this.capacidadCargador;
        return "Munición recargada a " + this.municionActual + " balas";
    }

    // Sobrecarga de recargar(): recarga parcial hasta completar el cargador
    public String recargar(int cantidad) {
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad a recargar debe ser positiva");
        this.municionActual = Math.min(capacidadCargador, municionActual + cantidad);
        return "Munición recargada a " + this.municionActual + " balas";
    }
}

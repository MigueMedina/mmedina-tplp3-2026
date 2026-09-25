package py.edu.uc.lp3.mm.cs2;

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
        this.dano = dano;
        this.precision = precision;
        this.recarga = recarga;
        this.velocidad = velocidad;
        this.capacidadCargador = capacidadCargador;
        this.municionActual = capacidadCargador;
    }

    // Getters y Setters
    public int getDano() { return dano; }
    public void setDano(int dano) { this.dano = dano; }

    public int getPrecision() { return precision; }
    public void setPrecision(int precision) { this.precision = precision; }

    public double getRecarga() { return recarga; }
    public void setRecarga(double recarga) { this.recarga = recarga; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }

    public int getMunicionActual() { return municionActual; }
    public void setMunicionActual(int municionActual) { this.municionActual = municionActual; }

    public int getCapacidadCargador() { return capacidadCargador; }
    public void setCapacidadCargador(int capacidadCargador) { this.capacidadCargador = capacidadCargador; }

    @Override
    public String recargar() {
        this.municionActual = this.capacidadCargador;
        return "Munición recargada a " + this.municionActual + " balas";
    }
}
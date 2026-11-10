package py.edu.uc.lp3.mm.cs2.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PISTOLA")
public class Pistola extends ArmaDeFuego {
    private boolean automatica;
    private boolean silenciador;

    protected Pistola() {
        this("Pistola genérica", 0, 0);
    }

    public Pistola(String nombre, int id, double precio) {
        this(nombre, id, precio, 0, 0, 0, 0, 0, false, false);
    }

    public Pistola(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                   int capacidadCargador, boolean automatica, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador);
        this.automatica = automatica;
        this.silenciador = silenciador;
    }

    // Getters y Setters
    public boolean isAutomatica() { return automatica; }
    public void setAutomatica(boolean automatica) { this.automatica = automatica; }

    public boolean isSilenciador() { return silenciador; }
    public void setSilenciador(boolean silenciador) { this.silenciador = silenciador; }

    @Override
    public String disparar() {
        return dispararConMunicion("¡Pum! Disparo de la pistola " + getNombre() + ".");
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Pistola: " + getNombre() + " | Daño: " + getDano() + " | Precio: $" + getPrecio();
    }
}

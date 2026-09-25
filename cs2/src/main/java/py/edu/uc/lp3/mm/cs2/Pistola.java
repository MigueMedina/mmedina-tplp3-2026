package py.edu.uc.lp3.mm.cs2;

public class Pistola extends ArmaDeFuego {
    private boolean automatica;
    private boolean silenciador;

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
        return "¡Pum! Disparo de la pistola " + getNombre();
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Pistola: " + getNombre() + " | Daño: " + getDano() + " | Precio: $" + getPrecio();
    }
}
package py.edu.uc.lp3.mm.cs2;

public class Fusil extends ArmaDeFuego {
    private boolean automatica;
    private int mira;
    private int retroceso;
    private boolean silenciador;

    public Fusil(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                 int capacidadCargador, boolean automatica, int mira, int retroceso, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador);
        this.automatica = automatica;
        this.mira = mira;
        this.retroceso = retroceso;
        this.silenciador = silenciador;
    }

    // Getters y Setters
    public boolean isAutomatica() { return automatica; }
    public void setAutomatica(boolean automatica) { this.automatica = automatica; }

    public int getMira() { return mira; }
    public void setMira(int mira) { this.mira = mira; }

    public int getRetroceso() { return retroceso; }
    public void setRetroceso(int retroceso) { this.retroceso = retroceso; }

    public boolean isSilenciador() { return silenciador; }
    public void setSilenciador(boolean silenciador) { this.silenciador = silenciador; }

    @Override
    public String disparar() {
        return "¡Bang! Ráfaga del fusil " + getNombre() + " (retroceso " + retroceso + ")";
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Fusil: " + getNombre() + " | Daño: " + getDano() + " | Precio: $" + getPrecio();
    }
}
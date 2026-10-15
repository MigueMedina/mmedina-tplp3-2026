package py.edu.uc.lp3.mm.cs2.domain;

public class AWP extends ArmaDeFuego implements UICapable {
    private int mira;
    private int retroceso;
    private boolean silenciador;

    public AWP() {
        super("", 0, 0, 0, 0, 0, 0, 0);
        this.mira = 0;
        this.retroceso = 0;
        this.silenciador = false;
    }

    public AWP(String nombre, int id, double precio) {
        super(nombre, id, precio, 0, 0, 0, 0, 0);
        this.mira = 0;
        this.retroceso = 0;
        this.silenciador = false;
    }

    public AWP(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
               int capacidadCargador, int mira, int retroceso, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador);
        this.mira = mira;
        this.retroceso = retroceso;
        this.silenciador = silenciador;
    }

    // Getters y Setters
    public int getMira() { return mira; }
    public void setMira(int mira) { this.mira = mira; }

    public int getRetroceso() { return retroceso; }
    public void setRetroceso(int retroceso) { this.retroceso = retroceso; }

    public boolean isSilenciador() { return silenciador; }
    public void setSilenciador(boolean silenciador) { this.silenciador = silenciador; }

    @Override
    public String disparar() {
        return "¡Crack! Disparo del AWP " + getNombre() + " (retroceso " + retroceso + ")";
    }

    @Override
    public String obtenerDetalleTienda() {
        return "AWP: " + getNombre() + " | Daño: " + getDano() + " | Precio: $" + getPrecio();
    }

    // UICapable: conserva el comportamiento default de mostrarEnUI()
    @Override
    public String getImagen() {
        return "awp_" + getNombre().toLowerCase().replace(" ", "_") + ".png";
    }

    @Override
    public String getSonido() {
        return "awp_disparo.ogg";
    }

    // UICapable: rango fijo del AWP (francotirador)
    @Override
    public double getRango() {
        return 100;
    }
}
package py.edu.uc.lp3.mm.cs2.domain;

public class AWP extends ArmaDeFuego implements UICapable {
    private int mira;
    private int retroceso;
    private boolean silenciador;

    public AWP() {
        this("AWP genérico", 0, 0);
    }

    public AWP(String nombre, int id, double precio) {
        this(nombre, id, precio, 0, 0, 0, 0, 0, 0, 0, false);
    }

    public AWP(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
               int capacidadCargador, int mira, int retroceso, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador);
        setMira(mira);
        setRetroceso(retroceso);
        this.silenciador = silenciador;
    }

    // Getters y Setters (con invariantes)
    public int getMira() { return mira; }
    public void setMira(int mira) {
        if (mira < 0)
            throw new IllegalArgumentException("La mira debe ser no negativa");
        this.mira = mira;
    }

    public int getRetroceso() { return retroceso; }
    public void setRetroceso(int retroceso) {
        if (retroceso < 0)
            throw new IllegalArgumentException("El retroceso debe ser no negativo");
        this.retroceso = retroceso;
    }

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

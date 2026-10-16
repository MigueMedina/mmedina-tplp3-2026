package py.edu.uc.lp3.mm.cs2.domain;

public class Fusil extends ArmaDeFuego implements UICapable {
    private boolean automatica;
    private int mira;
    private int retroceso;
    private boolean silenciador;

    public Fusil() {
        this("Fusil genérico", 0, 0);
    }

    public Fusil(String nombre, int id, double precio) {
        this(nombre, id, precio, 0, 0, 0, 0, 0, false, 0, 0, false);
    }

    public Fusil(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                 int capacidadCargador, boolean automatica, int mira, int retroceso, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador);
        this.automatica = automatica;
        setMira(mira);
        setRetroceso(retroceso);
        this.silenciador = silenciador;
    }

    // Getters y Setters (con invariantes)
    public boolean isAutomatica() { return automatica; }
    public void setAutomatica(boolean automatica) { this.automatica = automatica; }

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
        return "¡Bang! Ráfaga del fusil " + getNombre() + " (retroceso " + retroceso + ")";
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Fusil: " + getNombre() + " | Daño: " + getDano() + " | Precio: $" + getPrecio();
    }

    // UICapable: conserva el comportamiento default de mostrarEnUI()
    @Override
    public String getImagen() {
        return "fusil_" + getNombre().toLowerCase().replace(" ", "_") + ".png";
    }

    @Override
    public String getSonido() {
        return "fusil_disparo.ogg";
    }

    // UICapable: rango fijo del fusil
    @Override
    public double getRango() {
        return 30;
    }
}

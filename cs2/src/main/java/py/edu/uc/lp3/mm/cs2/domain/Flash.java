package py.edu.uc.lp3.mm.cs2.domain;

public class Flash extends Arrojadiza implements UICapable {
    private int intensidad;
    private double duracionCeguera;

    public Flash() {
        this("Flash genérica", 0, 0);
    }

    public Flash(String nombre, int id, double precio) {
        this(nombre, id, precio, "", 0, 0, 0, 0, 0, 0);
    }

    public Flash(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                 double cooldown, int intensidad, double duracionCeguera) {
        super(nombre, id, precio, tipo, radio, distancia, duracion, cooldown);
        setIntensidad(intensidad);
        setDuracionCeguera(duracionCeguera);
    }

    // Getters y Setters (con invariantes)
    public int getIntensidad() { return intensidad; }
    public void setIntensidad(int intensidad) {
        if (intensidad < 0)
            throw new IllegalArgumentException("La intensidad debe ser no negativa");
        this.intensidad = intensidad;
    }

    public double getDuracionCeguera() { return duracionCeguera; }
    public void setDuracionCeguera(double duracionCeguera) {
        if (duracionCeguera < 0)
            throw new IllegalArgumentException("La duración de ceguera debe ser no negativa");
        this.duracionCeguera = duracionCeguera;
    }

    @Override
    public String disparar() {
        return "¡Flash! La granada " + getNombre() + " ciega con intensidad " + intensidad;
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Flash: " + getNombre() + " | Intensidad: " + intensidad + " | Precio: $" + getPrecio();
    }

    // UICapable: sobrescribe el comportamiento default con lógica propia
    @Override
    public String getImagen() {
        return "flash_" + getNombre().toLowerCase().replace(" ", "_") + ".png";
    }

    @Override
    public String getSonido() {
        return "flash_cegadora.ogg";
    }

    @Override
    public String mostrarEnUI() {
        return "¡" + getNombre() + " impacta en pantalla con intensidad " + intensidad
                + " y ceguera de " + duracionCeguera + "s!";
    }

    // UICapable: las arrojadizas no "disparan", su cadencia deriva del cooldown
    @Override
    public double getVelocidadDisparo() {
        return getCooldown() > 0 ? 1.0 / getCooldown() : 0;
    }
}

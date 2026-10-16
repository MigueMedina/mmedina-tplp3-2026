package py.edu.uc.lp3.mm.cs2.domain;

public class Granada extends Arrojadiza implements UICapable {
    private int dano;
    private boolean aturdimiento;

    public Granada() {
        this("Granada genérica", 0, 0);
    }

    public Granada(String nombre, int id, double precio) {
        this(nombre, id, precio, "", 0, 0, 0, 0, 0, false);
    }

    public Granada(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                   double cooldown, int dano, boolean aturdimiento) {
        super(nombre, id, precio, tipo, radio, distancia, duracion, cooldown);
        setDano(dano);
        this.aturdimiento = aturdimiento;
    }

    // Getters y Setters (con invariantes)
    public int getDano() { return dano; }
    public void setDano(int dano) {
        if (dano < 0)
            throw new IllegalArgumentException("El daño debe ser no negativo");
        this.dano = dano;
    }

    public boolean isAturdimiento() { return aturdimiento; }
    public void setAturdimiento(boolean aturdimiento) { this.aturdimiento = aturdimiento; }

    @Override
    public String disparar() {
        return "¡Boom! La granada " + getNombre() + " explota causando " + dano + " de daño";
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Granada: " + getNombre() + " | Daño: " + dano + " | Precio: $" + getPrecio();
    }

    // UICapable: sobrescribe el comportamiento default con lógica propia
    @Override
    public String getImagen() {
        return "granada_" + getNombre().toLowerCase().replace(" ", "_") + ".png";
    }

    @Override
    public String getSonido() {
        return "granada_explosion.ogg";
    }

    @Override
    public String mostrarEnUI() {
        return "¡" + getNombre() + " explosiona con " + dano + " de daño"
                + (aturdimiento ? " y aturde al enemigo!" : "!");
    }

    // UICapable: las arrojadizas no "disparan", su cadencia deriva del cooldown
    @Override
    public double getVelocidadDisparo() {
        return getCooldown() > 0 ? 1.0 / getCooldown() : 0;
    }
}

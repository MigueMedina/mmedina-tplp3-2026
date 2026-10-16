package py.edu.uc.lp3.mm.cs2.domain;

public class Escopeta extends ArmaDeFuego {
    private int perdigon;
    private double dispersion;

    public Escopeta() {
        this("Escopeta genérica", 0, 0);
    }

    public Escopeta(String nombre, int id, double precio) {
        this(nombre, id, precio, 0, 0, 0, 0, 0, 0, 0);
    }

    public Escopeta(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                    int capacidadCargador, int perdigon, double dispersion) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador);
        setPerdigon(perdigon);
        setDispersion(dispersion);
    }

    // Getters y Setters (con invariantes)
    public int getPerdigon() { return perdigon; }
    public void setPerdigon(int perdigon) {
        if (perdigon < 0)
            throw new IllegalArgumentException("La cantidad de perdigones debe ser no negativa");
        this.perdigon = perdigon;
    }

    public double getDispersion() { return dispersion; }
    public void setDispersion(double dispersion) {
        if (dispersion < 0)
            throw new IllegalArgumentException("La dispersión debe ser no negativa");
        this.dispersion = dispersion;
    }

    @Override
    public String disparar() {
        return "¡Boom! Disparo de la escopeta " + getNombre() + " con " + perdigon + " perdigones";
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Escopeta: " + getNombre() + " | Daño: " + getDano() + " | Precio: $" + getPrecio();
    }
}

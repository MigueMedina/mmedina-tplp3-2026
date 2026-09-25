package py.edu.uc.lp3.mm.cs2;

public class Escopeta extends ArmaDeFuego {
    private int perdigon;
    private double dispersion;

    public Escopeta(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                    int capacidadCargador, int perdigon, double dispersion) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, capacidadCargador);
        this.perdigon = perdigon;
        this.dispersion = dispersion;
    }

    // Getters y Setters
    public int getPerdigon() { return perdigon; }
    public void setPerdigon(int perdigon) { this.perdigon = perdigon; }

    public double getDispersion() { return dispersion; }
    public void setDispersion(double dispersion) { this.dispersion = dispersion; }

    @Override
    public String disparar() {
        return "¡Boom! Disparo de la escopeta " + getNombre() + " con " + perdigon + " perdigones";
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Escopeta: " + getNombre() + " | Daño: " + getDano() + " | Precio: $" + getPrecio();
    }
}
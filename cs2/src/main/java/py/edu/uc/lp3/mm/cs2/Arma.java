package py.edu.uc.lp3.mm.cs2;

public abstract class Arma {
    private String nombre;
    private int id;
    private double precio;

    public Arma(String nombre, int id, double precio) {
        this.nombre = nombre;
        this.id = id;
        this.precio = precio;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
package py.edu.uc.lp3.mm.cs2.domain;

public abstract class Arma {
    private String nombre;
    private int id;
    private double precio;

    public Arma(String nombre, int id, double precio) {
        setNombre(nombre);
        setId(id);
        setPrecio(precio);
    }

    // Getters y Setters (con invariantes)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("El id debe ser no negativo");
        this.id = id;
    }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        if (precio < 0)
            throw new IllegalArgumentException("El precio debe ser no negativo");
        this.precio = precio;
    }

    // Métodos abstractos
    public abstract String disparar();
    public abstract String recargar();
    public abstract String obtenerDetalleTienda();
}

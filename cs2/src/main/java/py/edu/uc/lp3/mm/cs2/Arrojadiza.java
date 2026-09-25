package py.edu.uc.lp3.mm.cs2;

public abstract class Arrojadiza extends Arma {
    private String tipo;
    private double radio;
    private double distancia;
    private double duracion;
    private double cooldown;

    public Arrojadiza(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                      double cooldown) {
        super(nombre, id, precio);
        this.tipo = tipo;
        this.radio = radio;
        this.distancia = distancia;
        this.duracion = duracion;
        this.cooldown = cooldown;
    }

    // Getters y Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getRadio() { return radio; }
    public void setRadio(double radio) { this.radio = radio; }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }

    public double getDuracion() { return duracion; }
    public void setDuracion(double duracion) { this.duracion = duracion; }

    public double getCooldown() { return cooldown; }
    public void setCooldown(double cooldown) { this.cooldown = cooldown; }

    @Override
    public String recargar() {
        return "Las armas arrojadizas son de un solo uso y no se recargan.";
    }
}
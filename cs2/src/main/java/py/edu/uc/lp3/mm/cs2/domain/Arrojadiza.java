package py.edu.uc.lp3.mm.cs2.domain;

public abstract class Arrojadiza extends Arma {
    private String tipo;
    private double radio;
    private double distancia;
    private double duracion;
    private double cooldown;

    public Arrojadiza(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                      double cooldown) {
        super(nombre, id, precio);
        setTipo(tipo);
        setRadio(radio);
        setDistancia(distancia);
        setDuracion(duracion);
        setCooldown(cooldown);
    }

    // Getters y Setters (con invariantes)
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) {
        if (tipo == null)
            throw new IllegalArgumentException("El tipo no puede ser nulo");
        this.tipo = tipo;
    }

    public double getRadio() { return radio; }
    public void setRadio(double radio) {
        if (radio < 0)
            throw new IllegalArgumentException("El radio debe ser no negativo");
        this.radio = radio;
    }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) {
        if (distancia < 0)
            throw new IllegalArgumentException("La distancia debe ser no negativa");
        this.distancia = distancia;
    }

    public double getDuracion() { return duracion; }
    public void setDuracion(double duracion) {
        if (duracion < 0)
            throw new IllegalArgumentException("La duración debe ser no negativa");
        this.duracion = duracion;
    }

    public double getCooldown() { return cooldown; }
    public void setCooldown(double cooldown) {
        if (cooldown < 0)
            throw new IllegalArgumentException("El cooldown debe ser no negativo");
        this.cooldown = cooldown;
    }

    // UICapable: la distancia a la que se arroja representa su rango
    public double getRango() {
        return distancia;
    }

    @Override
    public String recargar() {
        return "Las armas arrojadizas son de un solo uso y no se recargan.";
    }
}

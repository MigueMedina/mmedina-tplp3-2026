package py.edu.uc.lp3.mm.cs2.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HUMO")
public class Humo extends Arrojadiza {

    protected Humo() {
        this("Humo genérico", 0, 0);
    }

    public Humo(String nombre, int id, double precio) {
        this(nombre, id, precio, "", 0, 0, 0, 0);
    }

    public Humo(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                double cooldown) {
        super(nombre, id, precio, tipo, radio, distancia, duracion, cooldown);
    }

    @Override
    public String disparar() {
        return "¡Humo! La granada " + getNombre() + " cubre el área con radio " + getRadio();
    }

    @Override
    public String obtenerDetalleTienda() {
        return "Humo: " + getNombre() + " | Radio: " + getRadio() + " | Precio: $" + getPrecio();
    }
}

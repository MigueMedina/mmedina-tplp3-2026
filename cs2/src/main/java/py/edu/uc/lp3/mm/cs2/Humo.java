package py.edu.uc.lp3.mm.cs2;

public class Humo extends Arrojadiza {

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
package py.edu.uc.lp3.mm.cs2.rest.dto;

public record JugadorRequest(String nombre, Integer blindaje) {

    public JugadorRequest(String nombre) {
        this(nombre, null);
    }
}

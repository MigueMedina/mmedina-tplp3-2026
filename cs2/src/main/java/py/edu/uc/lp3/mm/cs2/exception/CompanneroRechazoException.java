package py.edu.uc.lp3.mm.cs2.exception;

import py.edu.uc.lp3.mm.cs2.client.RolCompanero;

public class CompanneroRechazoException extends DomainException {

    private final RolCompanero rechazadoPor;
    private final int statusRemoto;
    private final String cuerpoRemoto;

    public CompanneroRechazoException(RolCompanero rechazadoPor, int statusRemoto, String cuerpoRemoto) {
        super("Rechazo de " + rechazadoPor.getEtiqueta() + " (HTTP " + statusRemoto + ")");
        this.rechazadoPor = rechazadoPor;
        this.statusRemoto = statusRemoto;
        this.cuerpoRemoto = cuerpoRemoto;
    }

    public CompanneroRechazoException(RolCompanero rechazadoPor, int statusRemoto, String cuerpoRemoto,
                                      Throwable causa) {
        super("Rechazo de " + rechazadoPor.getEtiqueta() + " (HTTP " + statusRemoto + ")", causa);
        this.rechazadoPor = rechazadoPor;
        this.statusRemoto = statusRemoto;
        this.cuerpoRemoto = cuerpoRemoto;
    }

    public RolCompanero getRechazadoPor() {
        return rechazadoPor;
    }

    public int getStatusRemoto() {
        return statusRemoto;
    }

    public String getCuerpoRemoto() {
        return cuerpoRemoto;
    }
}

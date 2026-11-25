package py.edu.uc.lp3.mm.cs2.exception;

import py.edu.uc.lp3.mm.cs2.client.RolCompanero;

public class CompaneroNoDisponibleException extends DomainException {

    private final RolCompanero companero;
    private final int statusHttp;
    private final int statusRemoto;
    private final String cuerpoRemoto;

    public CompaneroNoDisponibleException(RolCompanero companero, int statusHttp, int statusRemoto,
                                          String mensaje, String cuerpoRemoto, Throwable causa) {
        super(mensaje, causa);
        this.companero = companero;
        this.statusHttp = statusHttp;
        this.statusRemoto = statusRemoto;
        this.cuerpoRemoto = cuerpoRemoto;
    }

    public RolCompanero getCompanero() {
        return companero;
    }

    public int getStatusHttp() {
        return statusHttp;
    }

    public int getStatusRemoto() {
        return statusRemoto;
    }

    public String getCuerpoRemoto() {
        return cuerpoRemoto;
    }
}

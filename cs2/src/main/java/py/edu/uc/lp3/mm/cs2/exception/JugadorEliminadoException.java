package py.edu.uc.lp3.mm.cs2.exception;

public class JugadorEliminadoException extends DomainException {

    public JugadorEliminadoException(String mensaje) {
        super(mensaje);
    }

    public JugadorEliminadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

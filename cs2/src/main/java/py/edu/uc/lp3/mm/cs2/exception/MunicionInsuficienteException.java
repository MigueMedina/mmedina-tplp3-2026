package py.edu.uc.lp3.mm.cs2.exception;

public class MunicionInsuficienteException extends DomainException {

    public MunicionInsuficienteException(String mensaje) {
        super(mensaje);
    }

    public MunicionInsuficienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

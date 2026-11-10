package py.edu.uc.lp3.mm.cs2.exception;

/**
 * Raíz de las excepciones propias del dominio CS2.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String mensaje) {
        super(mensaje);
    }

    protected DomainException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

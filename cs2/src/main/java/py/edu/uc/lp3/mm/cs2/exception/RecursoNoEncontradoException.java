package py.edu.uc.lp3.mm.cs2.exception;

/**
 * Se lanza cuando se busca una entidad por un identificador que no existe.
 */
public class RecursoNoEncontradoException extends DomainException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

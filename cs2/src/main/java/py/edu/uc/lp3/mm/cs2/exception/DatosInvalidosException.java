package py.edu.uc.lp3.mm.cs2.exception;

/**
 * Se lanza cuando los datos recibidos o la acción solicitada violan una regla
 * de negocio o un invariante del dominio (por ejemplo, precio negativo o
 * disparar a un jugador muerto).
 */
public class DatosInvalidosException extends DomainException {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }

    public DatosInvalidosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

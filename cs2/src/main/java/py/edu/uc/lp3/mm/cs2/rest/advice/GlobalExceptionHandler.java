package py.edu.uc.lp3.mm.cs2.rest.advice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.DomainException;
import py.edu.uc.lp3.mm.cs2.exception.JugadorEliminadoException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;

/**
 * Traduce las excepciones del dominio a respuestas HTTP concretas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> manejarRecursoNoEncontrado(RecursoNoEncontradoException excepcion,
                                                          HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, excepcion, request);
    }

    @ExceptionHandler(DatosInvalidosException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarDatosInvalidos(DatosInvalidosException excepcion,
                                                     HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, excepcion, request);
    }

    @ExceptionHandler(MunicionInsuficienteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> manejarMunicionInsuficiente(MunicionInsuficienteException excepcion,
                                                           HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, excepcion, request);
    }

    @ExceptionHandler(JugadorEliminadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> manejarJugadorEliminado(JugadorEliminadoException excepcion,
                                                       HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, excepcion, request);
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarErrorDeDominio(DomainException excepcion,
                                                     HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, excepcion, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarArgumentoInvalido(IllegalArgumentException excepcion,
                                                        HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, excepcion, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarJsonInvalido(HttpMessageNotReadableException excepcion,
                                                   HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo JSON de la petición es inválido", excepcion, request);
    }

    private Map<String, Object> respuesta(HttpStatus estado, RuntimeException excepcion,
                                          HttpServletRequest request) {
        return respuesta(estado, excepcion.getMessage(), excepcion, request);
    }

    private Map<String, Object> respuesta(HttpStatus estado, String mensaje,
                                          RuntimeException excepcion, HttpServletRequest request) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", estado.value());
        respuesta.put("error", estado.getReasonPhrase());
        respuesta.put("excepcion", excepcion.getClass().getSimpleName());
        respuesta.put("mensaje", mensaje);
        respuesta.put("path", request.getRequestURI());
        return respuesta;
    }
}

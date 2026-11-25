package py.edu.uc.lp3.mm.cs2.rest.advice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

import py.edu.uc.lp3.mm.cs2.exception.CompanneroRechazoException;
import py.edu.uc.lp3.mm.cs2.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.mm.cs2.exception.ConflictoDeEstadoException;
import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.DomainException;
import py.edu.uc.lp3.mm.cs2.exception.JugadorEliminadoException;
import py.edu.uc.lp3.mm.cs2.exception.MunicionInsuficienteException;
import py.edu.uc.lp3.mm.cs2.exception.RecursoNoEncontradoException;
import py.edu.uc.lp3.mm.cs2.rest.dto.ErrorCompaneroResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.ErrorResponse;
import py.edu.uc.lp3.mm.cs2.rest.dto.ErrorValidacionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(RecursoNoEncontradoException excepcion,
                                                                     HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, excepcion, request);
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<ErrorResponse> manejarDatosInvalidos(DatosInvalidosException excepcion,
                                                               HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, excepcion, request);
    }

    @ExceptionHandler(MunicionInsuficienteException.class)
    public ResponseEntity<ErrorResponse> manejarMunicionInsuficiente(MunicionInsuficienteException excepcion,
                                                                     HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, excepcion, request);
    }

    @ExceptionHandler(JugadorEliminadoException.class)
    public ResponseEntity<ErrorResponse> manejarJugadorEliminado(JugadorEliminadoException excepcion,
                                                                 HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, excepcion, request);
    }

    @ExceptionHandler(ConflictoDeEstadoException.class)
    public ResponseEntity<ErrorResponse> manejarConflictoDeEstado(ConflictoDeEstadoException excepcion,
                                                                  HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, excepcion, request);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> manejarBloqueoOptimista(OptimisticLockingFailureException excepcion,
                                                                 HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, "El estado cambió durante la operación; reintente",
                excepcion, request);
    }

    @ExceptionHandler(CompanneroRechazoException.class)
    public ResponseEntity<ErrorCompaneroResponse> manejarRechazoDeCompanero(CompanneroRechazoException excepcion,
                                                                           HttpServletRequest request) {
        ErrorResponse base = respuesta(HttpStatus.CONFLICT, excepcion, request).getBody();
        ErrorCompaneroResponse cuerpo = ErrorCompaneroResponse.desde(
                base,
                excepcion.getRechazadoPor().getEtiqueta(),
                excepcion.getStatusRemoto(),
                limitar(excepcion.getCuerpoRemoto()),
                base.status());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);
    }

    @ExceptionHandler(CompaneroNoDisponibleException.class)
    public ResponseEntity<ErrorCompaneroResponse> manejarCompaneroNoDisponible(
            CompaneroNoDisponibleException excepcion, HttpServletRequest request) {
        HttpStatus estado = HttpStatus.valueOf(excepcion.getStatusHttp());
        ErrorResponse base = respuesta(estado, excepcion, request).getBody();
        ErrorCompaneroResponse cuerpo = ErrorCompaneroResponse.desde(
                base,
                excepcion.getCompanero().getEtiqueta(),
                excepcion.getStatusRemoto(),
                limitar(excepcion.getCuerpoRemoto()),
                base.status());
        return ResponseEntity.status(estado).body(cuerpo);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> manejarErrorDeDominio(DomainException excepcion,
                                                               HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, excepcion, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> manejarArgumentoInvalido(IllegalArgumentException excepcion,
                                                                  HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, excepcion, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValidacionResponse> manejarValidacion(MethodArgumentNotValidException excepcion,
                                                                     HttpServletRequest request) {
        Map<String, String> errores = excepcion.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (primero, segundo) -> primero,
                        LinkedHashMap::new));
        String mensaje = errores.entrySet().stream()
                .map(error -> error.getKey() + ": " + error.getValue())
                .collect(Collectors.joining("; "));
        String campo = errores.keySet().stream().findFirst().orElse(null);
        ErrorResponse base = respuesta(HttpStatus.BAD_REQUEST, mensaje, excepcion, request).getBody();
        return ResponseEntity.badRequest().body(ErrorValidacionResponse.desde(base, campo, errores));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoDeParametroInvalido(MethodArgumentTypeMismatchException excepcion,
                                                                        HttpServletRequest request) {
        String mensaje = "El parámetro '" + excepcion.getName() + "' no tiene el tipo esperado";
        return respuesta(HttpStatus.BAD_REQUEST, mensaje, excepcion, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException excepcion,
                                                             HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo JSON de la petición es inválido", excepcion, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorInesperado(Exception excepcion,
                                                                HttpServletRequest request) {
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, excepcion, request);
    }

    private ResponseEntity<ErrorResponse> respuesta(HttpStatus estado, Exception excepcion,
                                                    HttpServletRequest request) {
        return respuesta(estado, excepcion.getMessage(), excepcion, request);
    }

    private ResponseEntity<ErrorResponse> respuesta(HttpStatus estado, String mensaje,
                                                    Exception excepcion, HttpServletRequest request) {
        ErrorResponse cuerpo = new ErrorResponse(
                LocalDateTime.now(),
                UUID.randomUUID().toString(),
                estado.value(),
                estado.getReasonPhrase(),
                excepcion.getClass().getSimpleName(),
                mensaje,
                request.getRequestURI());
        return ResponseEntity.status(estado).body(cuerpo);
    }

    private String limitar(String texto) {
        if (texto == null) {
            return null;
        }
        return texto.length() <= 1000 ? texto : texto.substring(0, 1000) + "...";
    }
}

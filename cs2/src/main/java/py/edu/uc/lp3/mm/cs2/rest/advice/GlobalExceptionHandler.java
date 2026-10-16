package py.edu.uc.lp3.mm.cs2.rest.advice;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Traduce las violaciones de invariantes del dominio
 * ({@link IllegalArgumentException}) a respuestas HTTP 400 con un mensaje claro.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarArgumentoInvalido(IllegalArgumentException excepcion, HttpServletRequest request) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        respuesta.put("mensaje", excepcion.getMessage());
        respuesta.put("path", request.getRequestURI());
        return respuesta;
    }
}

package co.com.pragma.api.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validación fallida");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleWebClientRequestException(WebClientRequestException ex) {
        Map<String, Object> response = new HashMap<>();
        Throwable cause = ex.getCause();
        if (cause instanceof ConnectException) {
            response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("error", "Error interno de conexión");
            response.put("message", "No se pudo conectar con el servicio requerido. Intente más tarde.");
            response.put("detail", cause.getMessage());
        } else {
            response.put("status", HttpStatus.BAD_GATEWAY.value());
            response.put("error", "Error de comunicación");
            response.put("message", ex.getMessage());
            response.put("detail", ex.getCause() != null ? ex.getCause().getMessage() : "");
        }
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getCause();
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error en formato de datos");

        if (rootCause instanceof InvalidFormatException invalidEx
                && invalidEx.getCause() instanceof DateTimeParseException dateEx) {
            body.put("message", "Formato de fecha inválido, se esperaba 'yyyy-MM-dd' o 'yyyy-MM-ddTHH:mm:ss'");
            body.put("detail", dateEx.getParsedString());
        } else {
            body.put("message", rootCause != null ? rootCause.getClass().getSimpleName() : ex.getClass().getSimpleName());
            body.put("detail", rootCause != null ? rootCause.getMessage() : ex.getMessage());
        }

        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Map<String, Object>> handleServerWebInput(ServerWebInputException ex) {
        Throwable rootCause = ex.getCause();
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Error de entrada de dato");
        body.put("message", rootCause != null ? rootCause.getClass().getSimpleName() : ex.getClass().getSimpleName());
        body.put("detail", rootCause != null ? rootCause.getMessage() : ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleWebExchangeBindException(WebExchangeBindException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validación fallida");
        response.put("message", ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Error de negocio");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(response));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGeneralException(Exception ex) {
        Throwable rootCause = ex.getCause();
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Error inesperado");
        response.put("message", rootCause != null ? rootCause.getClass().getSimpleName() : ex.getClass().getSimpleName());
        response.put("detail", rootCause != null ? rootCause.getMessage() : ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
    }

}
package elorServ.modelo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Creamos la instancia del logger
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Manejo de "No Encontrado" (404)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex, HttpServletRequest request) {
        // Log de advertencia (no es un error crítico del sistema)
        logger.warn("Recurso no encontrado: {} en la ruta {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 2. Manejo de Errores Inesperados (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalError(Exception ex, HttpServletRequest request) {
        // IMPORTANTE: Log del error con la traza completa para poder debuguear
        logger.error("ERROR INTERNO DEL SERVIDOR: ", ex);
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ha ocurrido un error inesperado. Por favor, contacte con soporte.",
                System.currentTimeMillis(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
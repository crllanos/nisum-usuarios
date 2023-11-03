package cl.nisum.usuarios.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
public class NisumExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<NisumException> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(String.format("Bad request: %s.", e.getMessage()));
        return ResponseEntity.badRequest().body(NisumException.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<NisumException> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(String.format("Not found: %s.", e.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(NisumException.builder()
                        .message(e.getMessage())
                        .build());
    }
}
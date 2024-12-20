package dev.gabrafo.libraryweb.errors.handlers;

import dev.gabrafo.libraryweb.errors.exceptions.ClientUnavailableException;
import dev.gabrafo.libraryweb.errors.exceptions.InvalidEntryException;
import dev.gabrafo.libraryweb.errors.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<RestErrorMessage> entityNotFoundException(NotFoundException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(InvalidEntryException.class)
    public ResponseEntity<RestErrorMessage> invalidEntryException(InvalidEntryException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(ClientUnavailableException.class)
    public ResponseEntity<RestErrorMessage> clientException(ClientUnavailableException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }
}
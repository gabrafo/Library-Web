package dev.gabrafo.libraryweb.errors.handlers;

import dev.gabrafo.libraryweb.errors.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<RestErrorMessage> clientUnavailableException(ClientUnavailableException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorMessage> handleValidationExceptions(MethodArgumentNotValidException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, "Algum dos campos não foi preenchido validamente.");
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(ExistentEmailException.class)
    public ResponseEntity<RestErrorMessage> existentEmailException(ExistentEmailException e){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(ExistentBookException.class)
    public ResponseEntity<RestErrorMessage> existentBookException(ExistentBookException e){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(InvalidLoanException.class)
    public ResponseEntity<RestErrorMessage> invalidLoanException(InvalidLoanException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(InvalidReturnOperationException.class)
    public ResponseEntity<RestErrorMessage> invalidReturnOperationException(InvalidReturnOperationException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(AlreadyVerifiedException.class)
    public ResponseEntity<RestErrorMessage> alreadyVerifiedException(AlreadyVerifiedException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<RestErrorMessage> authorizationDeniedException(){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.FORBIDDEN, "Acesso negado!");
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestErrorMessage> badCredentialsException(){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.FORBIDDEN, "Credenciais inválidas!");
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }
}
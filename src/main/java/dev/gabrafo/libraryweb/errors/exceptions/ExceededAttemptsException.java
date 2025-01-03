package dev.gabrafo.libraryweb.errors.exceptions;

public class ExceededAttemptsException extends RuntimeException{
    public ExceededAttemptsException(String message) {
        super(message);
    }
}

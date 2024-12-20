package dev.gabrafo.libraryweb.errors.exceptions;

public class ExistentEmailException extends RuntimeException{
    public ExistentEmailException(String message) {
        super(message);
    }
}

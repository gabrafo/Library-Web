package dev.gabrafo.libraryweb.errors.handlers;

import org.springframework.http.HttpStatus;

import java.util.Date;

public record RestErrorMessage(
        HttpStatus status,
        String msg,
        Date timestamp
) {
    public RestErrorMessage(HttpStatus status, String msg){
        this(status, msg, new Date());
    }
}
package dev.gabrafo.libraryweb.features.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO (
        @NotEmpty @Email
        String email,

        @NotEmpty
        String password
){}
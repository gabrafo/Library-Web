package dev.gabrafo.libraryweb.features.user;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;
import java.time.Period;

public record UserRequestDTO(
        @NotEmpty
        String name,

        @NotEmpty @Email(message = "Email deve ser válido!")
        String email,

        @NotEmpty
        String password,

        @NotEmpty
        String role,

        @NotNull
        @Past
        LocalDate birthDate,

        @NotEmpty
        String zipCode
) {
    @AssertTrue(message = "User must be at least 18 years old")
    public boolean isAdult() {
        // Verifica se a data de nascimento tem pelo menos 18 anos
        return birthDate != null && Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }

    @Builder
    public UserRequestDTO(User user){
        this(
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getRole().toString(),
            user.getBirthDate(),
            user.getAddress() != null ? user.getAddress().getZipCode() : null
        );
    }
}

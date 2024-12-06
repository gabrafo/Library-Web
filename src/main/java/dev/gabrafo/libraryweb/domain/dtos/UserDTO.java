package dev.gabrafo.libraryweb.domain.dtos;

import dev.gabrafo.libraryweb.domain.entities.Address;
import dev.gabrafo.libraryweb.domain.entities.Loan;
import dev.gabrafo.libraryweb.domain.entities.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public record UserDTO(
        @NotEmpty
        String name,

        @NotEmpty
        @Past
        LocalDate birthDate,

        @NotEmpty
        Address address,

        List<Loan> loans
) {
    @AssertTrue(message = "User must be at least 18 years old")
    public boolean isAdult() {
        // Verifica se a data de nascimento tem pelo menos 18 anos
        return birthDate != null && Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }

    @Builder
    public UserDTO(User user){
        this(
            user.getName(),
            user.getBirthDate(),
            user.getAddress(),
            user.getLoans()
        );
    }
}

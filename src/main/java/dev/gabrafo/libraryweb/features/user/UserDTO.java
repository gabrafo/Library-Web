package dev.gabrafo.libraryweb.features.user;

import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.loan.Loan;
import dev.gabrafo.libraryweb.enums.Role;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public record UserDTO(
        @NotEmpty
        String name,

        @NotEmpty @Email
        String email,

        @NotEmpty
        String password,

        @NotEmpty
        Role role,

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
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            user.getBirthDate(),
            user.getAddress(),
            user.getLoans()
        );
    }

    public record UserResponse(String name, String email, Role role) {
        public UserResponse(User user) {
            this(user.getName(), user.getEmail(), user.getRole());
        }
    }
}

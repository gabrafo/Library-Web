package dev.gabrafo.libraryweb.features.user;

import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.loan.Loan;
import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.features.user.security.UserLoginDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private LocalDate birthDate;

    // Usuário referencia endereço, mas não o contrário. Motivo: Permitir reutilização de endereços iguais para diferentes usuários.
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();

    @Builder
    public User(UserDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = dto.password();
        this.role = dto.role();
        this.birthDate = dto.birthDate();
        this.address = dto.address();
        this.loans = dto.loans();
    }

    public boolean isLoginCorrect(UserLoginDTO userLoginDTO, PasswordEncoder encoder){
        return(encoder.matches(userLoginDTO.password(), this.password));
    }
}

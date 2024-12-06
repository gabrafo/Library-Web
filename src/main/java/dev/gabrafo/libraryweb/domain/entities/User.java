package dev.gabrafo.libraryweb.domain.entities;

import dev.gabrafo.libraryweb.domain.dtos.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDate birthDate;

    // Usuário referencia endereço, mas não o contrário. Motivo: Permitir reutilização de endereços iguais para diferentes usuários.
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();

    public User(UserDTO userDTO) {
        this.name = userDTO.name();
        this.birthDate = userDTO.birthDate();
        this.address = userDTO.address();
        this.loans = userDTO.loans();
    }
}

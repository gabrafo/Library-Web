package dev.gabrafo.libraryweb.features.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.book.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    // Usuário referencia endereço, mas não o contrário. Motivo: Permitir reutilização de endereços iguais para diferentes usuários.
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", nullable = false)
    private Address address;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "tb_user_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Book> borrowedBooks = new ArrayList<>();

    private boolean isEmailVerified;

    public User(UserRequestDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = dto.password();
        this.role = Role.valueOf(dto.role());
        this.birthDate = dto.birthDate();
    }

    public User(UserResponseDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.borrowedBooks = dto.borrowedBooks();
        this.role = dto.role();
        this.birthDate = dto.birthDate();
        this.address = dto.address();
    }

    public User(String name, String email, String password, Role role, LocalDate birthDate,
                Address address, List<Book> borrowedBooks, boolean isEmailVerified) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.birthDate = birthDate;
        this.address = address;
        this.borrowedBooks = borrowedBooks;
        this.isEmailVerified = isEmailVerified;
    }

    public boolean isLoginCorrect(UserLoginDTO userLoginDTO, PasswordEncoder encoder) {
        return (encoder.matches(userLoginDTO.password(), this.password));
    }
}
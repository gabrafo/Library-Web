package dev.gabrafo.libraryweb.features.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.gabrafo.libraryweb.features.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @NotNull(message = "ISBN cannot be null")
    @Pattern(
            regexp = "^(97[89]\\d{10})$",
            message = "Invalid ISBN-13 format, should be 13 digits starting with 978 or 979"
    )
    @Column(name = "isbn", nullable = false, unique = true, length = 13)
    private String isbn;

    private String title;

    // Abaixo criarei tabelas separadas p/ autores por ser um atr multivalorado
    @ElementCollection
    @CollectionTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"))
    private List<String> authors;

    private String publisher;

    private LocalDate releaseDate;

    private int quantity;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "tb_user_book",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<User> borrowedBy;

    public Book(BookResponseDTO dto){
        this.bookId = dto.id();
        this.isbn = dto.isbn();
        this.title = dto.title();
        this.authors = dto.authors();
        this.quantity = dto.quantity();
        this.publisher = dto.publisher();
        this.releaseDate = dto.releaseDate();
    }

    public Book(BookRequestDTO dto){
        this.isbn = dto.isbn();
        this.title = dto.title();
        this.authors = dto.authors();
        this.quantity = dto.quantity();
        this.publisher = dto.publisher();
        this.releaseDate = dto.releaseDate();
    }

    public Book(String isbn, String title, List<String> authors, String publisher, LocalDate releaseDate,
                int quantity, List<User> borrowedBy) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.quantity = quantity;
        this.borrowedBy = borrowedBy;
    }
}
package dev.gabrafo.libraryweb.domain.dtos;

import dev.gabrafo.libraryweb.domain.entities.Book;
import dev.gabrafo.libraryweb.domain.entities.Loan;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record BookDTO (
        @NotEmpty
        @Pattern(
                regexp = "^(97[89]\\d{10})$",
                message = "Invalid ISBN-13 format, should be 13 digits starting with 978 or 979"
        )
        String isbn,

        @NotEmpty
        String title,

        List<String> authors,

        String publisher,

        LocalDate releaseDate,

        List<Loan> loans
){
    @Builder
    public BookDTO(Book book) {
        this(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthors(),
                book.getPublisher(),
                book.getReleaseDate(),
                book.getLoans()
        );
    }
}
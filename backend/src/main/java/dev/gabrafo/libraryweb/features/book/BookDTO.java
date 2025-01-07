package dev.gabrafo.libraryweb.features.book;

import dev.gabrafo.libraryweb.features.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record BookDTO(
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

        @Positive
        int quantity,

        LocalDate releaseDate,

        List<String> emailBorrowedBy
){
    @Builder
    public BookDTO(Book book) {
        this(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthors(),
                book.getPublisher(),
                book.getQuantity(),
                book.getReleaseDate(),
                book.getBorrowedBy().stream()
                        .map(User::getEmail)
                        .collect(Collectors.toList())
        );
    }
}
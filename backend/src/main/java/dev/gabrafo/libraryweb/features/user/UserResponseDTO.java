package dev.gabrafo.libraryweb.features.user;

import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.book.Book;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDTO(Long id, String name, String email, Role role, LocalDate birthDate, Address address, List<Book> borrowedBooks) {
    @Builder
    public UserResponseDTO(User user) {
        this(user.getUserId(), user.getName(), user.getEmail(), user.getRole(), user.getBirthDate(), user.getAddress(), user.getBorrowedBooks());
    }
}
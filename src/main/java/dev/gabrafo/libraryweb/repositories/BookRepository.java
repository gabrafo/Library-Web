package dev.gabrafo.libraryweb.repositories;

import dev.gabrafo.libraryweb.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
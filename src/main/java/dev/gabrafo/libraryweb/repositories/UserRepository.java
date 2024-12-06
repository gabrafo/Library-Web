package dev.gabrafo.libraryweb.repositories;

import dev.gabrafo.libraryweb.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
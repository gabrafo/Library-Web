package dev.gabrafo.libraryweb.repositories;

import dev.gabrafo.libraryweb.domain.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
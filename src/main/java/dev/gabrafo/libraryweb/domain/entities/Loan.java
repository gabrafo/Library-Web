package dev.gabrafo.libraryweb.domain.entities;

import dev.gabrafo.libraryweb.domain.dtos.LoanDTO;
import dev.gabrafo.libraryweb.domain.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "tb_user_book")
@NoArgsConstructor
@Getter
@Setter
public class Loan {

    @Id
    private Long loanId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate loanDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // "PENDING", "RETURNED"

    public Loan(LoanDTO loanDTO, User user, Book book) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDTO.loanDate();
        this.returnDate = loanDTO.returnDate();
        this.status = loanDTO.status();
    }
}

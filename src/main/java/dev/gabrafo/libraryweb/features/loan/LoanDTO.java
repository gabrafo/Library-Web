package dev.gabrafo.libraryweb.features.loan;

import dev.gabrafo.libraryweb.features.book.Book;
import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.enums.Status;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDate;

public record LoanDTO(
        @NotEmpty
        User user,

        @NotEmpty
        Book book,

        @NotEmpty
        @PastOrPresent
        LocalDate loanDate,

        LocalDate returnDate,

        @NotEmpty
        Status status
) {
    @AssertTrue(message = "Status must be LOANED or AVAILABLE")
    public boolean isValidStatus() {
        return status == Status.LOANED || status == Status.AVAILABLE;
    }

    @Builder
    public LoanDTO(Loan loan) {
        this(loan.getUser(), loan.getBook(), loan.getLoanDate(), loan.getReturnDate(), loan.getStatus());
    }
}

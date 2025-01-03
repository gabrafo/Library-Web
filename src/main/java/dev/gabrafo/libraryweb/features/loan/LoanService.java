package dev.gabrafo.libraryweb.features.loan;

import dev.gabrafo.libraryweb.errors.exceptions.InvalidLoanException;
import dev.gabrafo.libraryweb.errors.exceptions.InvalidReturnOperationException;
import dev.gabrafo.libraryweb.errors.exceptions.NotFoundException;
import dev.gabrafo.libraryweb.features.book.Book;
import dev.gabrafo.libraryweb.features.book.BookRepository;
import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.features.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public String borrowBook(Long bookId, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado!"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Livro não encontrado!"));

        if(book.getQuantity() < 1) {
            throw new InvalidLoanException(book.getTitle() + " não tem cópias disponíveis para empréstimo.");
        }

        if(user.getBorrowedBooks().contains(book)) {
            throw new InvalidLoanException(book.getTitle() + " já foi emprestado ao usuário.");
        }

        book.setQuantity(book.getQuantity()-1);

        user.getBorrowedBooks().add(book);
        book.getBorrowedBy().add(user);

        userRepository.save(user);
        bookRepository.save(book);
        return book.getTitle() + " emprestado ao usuário " + user.getName() + "!";
    }

    @Transactional
    public String returnBook(Long bookId, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado!"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Livro não encontrado!"));

        if(!user.getBorrowedBooks().contains(book)) {
            throw new InvalidReturnOperationException(book.getTitle() + " não foi emprestado ao usuário.");
        }

        book.setQuantity(book.getQuantity()+1);

        user.getBorrowedBooks().remove(book);
        book.getBorrowedBy().remove(user);

        userRepository.save(user);
        bookRepository.save(book);
        return book.getTitle() + " devolvido por " + user.getName() + "!";
    }
}

package dev.gabrafo.libraryweb.features.book;

import dev.gabrafo.libraryweb.errors.exceptions.ExistentBookException;
import dev.gabrafo.libraryweb.errors.exceptions.NotFoundException;
import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.features.user.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper mapper;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, BookMapper mapper, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createBook(BookRequestDTO dto){
        Book newBook = mapper.fromRequestToEntity(dto);
        if(bookRepository.findByIsbn(dto.isbn()).isPresent()) throw new ExistentBookException("Livro com ISBN '" + dto.isbn() +  "' já existente.");
        bookRepository.save(newBook);
    }

    @Transactional
    public void addBookUnit(BookAdditionDTO dto){
        Optional<Book> book = bookRepository.findByIsbn(dto.isbn());
        String isbn = dto.isbn();
        if(book.isEmpty()) throw new NotFoundException("Livro com ISBN '" + isbn +  "' inexistente.");
        book.get().setQuantity(book.get().getQuantity()+dto.units());
    }

    public List<BookResponseDTO> findAlBooks(){
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookResponseDTO::new).collect(Collectors.toList());
    }

    public BookResponseDTO findBookById(Long id){
        return mapper.toResponseDTO(bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado!")));
    }

    @Transactional
    public BookResponseDTO updateBookById(Long id, @Valid BookRequestDTO dto){
        Book updatedBook = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado! Não haverá atualização."));

        List<String> borrowedEmails = updatedBook.getBorrowedBy().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        List<User> users = userRepository.findAllByEmailIn(borrowedEmails);

        updatedBook.setAuthors(dto.authors());
        updatedBook.setIsbn(dto.isbn());
        updatedBook.setBorrowedBy(users);
        updatedBook.setTitle(dto.title());
        updatedBook.setReleaseDate(dto.releaseDate());
        updatedBook.setPublisher(dto.publisher());

        return mapper.toResponseDTO(updatedBook);
    }

    @Transactional
    public String deleteBookById(Long id){
        Book deleted = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado!"));

        String isbn = deleted.getIsbn();

        bookRepository.delete(deleted);
        return "Livro com ISBN: '" + isbn + "' deletado!";
    }
}

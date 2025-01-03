package dev.gabrafo.libraryweb.features.book;

import dev.gabrafo.libraryweb.errors.exceptions.ExistentBookException;
import dev.gabrafo.libraryweb.errors.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public BookService(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public void createBook(BookDTO dto){
        Book newBook = mapper.toEntity(dto);
        if(repository.findByIsbn(dto.isbn()).isPresent()) throw new ExistentBookException("Livro com ISBN '" + dto.isbn() +  "' já existente.");
        repository.save(newBook);
    }

    @Transactional
    public void addBookUnit(BookAdditionDTO dto){
        Optional<Book> book = repository.findByIsbn(dto.isbn());
        String isbn = dto.isbn();
        if(book.isEmpty()) throw new NotFoundException("Livro com ISBN '" + isbn +  "' inexistente.");
        book.get().setQuantity(book.get().getQuantity()+dto.units());
    }

    public List<BookDTO> findAlBooks(){
        List<Book> books = repository.findAll();
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    public BookDTO findBookById(Long id){
        return mapper.toDTO(repository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado!")));
    }

    @Transactional
    public BookDTO updateBookById(Long id, BookDTO dto){
        Book updatedBook = repository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado! Não haverá atualização."));

        updatedBook.setAuthors(dto.authors());
        updatedBook.setIsbn(dto.isbn());
        updatedBook.setBorrowedBy(dto.borrowedBy());
        updatedBook.setTitle(dto.title());
        updatedBook.setReleaseDate(dto.releaseDate());
        updatedBook.setPublisher(dto.publisher());

        return mapper.toDTO(updatedBook);
    }

    @Transactional
    public String deleteBookById(Long id){
        Book deleted = repository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado!"));

        String isbn = deleted.getIsbn();

        repository.delete(deleted);
        return "Livro com ISBN: '" + isbn + "' deletado!";
    }
}

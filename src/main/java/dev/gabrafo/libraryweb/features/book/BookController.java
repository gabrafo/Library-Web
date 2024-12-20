package dev.gabrafo.libraryweb.features.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Tag(name = "Livro", description = "Endpoints relacionados a gerenciamento de livros.")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping(value = "/all", produces = "application/json")
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista com todos os livros disponíveis.")
    @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDTO.class)))
    public ResponseEntity<List<BookDTO>> findAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAlBooks());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Buscar livro por ID", description = "Retorna os detalhes de um livro específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<BookDTO> findBookById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findBookById(id));
    }

    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Criar um novo livro", description = "Adiciona um novo livro ao sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<Void> createBook(@Valid @RequestBody BookDTO dto) {
        service.createBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/update/{id}", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Atualizar livro", description = "Atualiza os detalhes de um livro existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<BookDTO> updateBookById(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateBookById(id, dto));
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Excluir livro", description = "Remove um livro do sistema pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<String> deleteBookByid(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteBookById(id));
    }
}
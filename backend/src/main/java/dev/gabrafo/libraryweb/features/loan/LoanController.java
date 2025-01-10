package dev.gabrafo.libraryweb.features.loan;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
@Tag(name = "Empréstimos", description = "Endpoints relacionados ao gerenciamento de empréstimos de livros.")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/borrow")
    @Operation(summary = "Empréstimo de livro", description = "Realiza o empréstimo de um livro para um usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro emprestado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro no empréstimo do livro"),
            @ApiResponse(responseCode = "404", description = "Usuário ou livro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PreAuthorize("@userService.isAdmin(authentication.name) or @userService.isUserIdOwner(#userId, authentication.name)")
    public ResponseEntity<String> borrowBook(@RequestParam Long bookId, @RequestParam Long userId) {
        String message = loanService.borrowBook(bookId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @PutMapping("/return")
    @Operation(summary = "Devolução de livro", description = "Realiza a devolução de um livro por parte de um usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro devolvido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na devolução do livro"),
            @ApiResponse(responseCode = "404", description = "Usuário ou livro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PreAuthorize("@userService.isAdmin(authentication.name) or @userService.isUserIdOwner(#userId, authentication.name)")
    public ResponseEntity<String> returnBook(@RequestParam Long bookId, @RequestParam Long userId) {
        String message = loanService.returnBook(bookId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
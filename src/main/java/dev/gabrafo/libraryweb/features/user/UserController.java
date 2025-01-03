package dev.gabrafo.libraryweb.features.user;

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
@RequestMapping("/user")
@Tag(name = "Usuário", description = "Endpoints relacionados ao gerenciamento de usuários.")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar um novo usuário", description = "Cria um novo usuário no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "Email já em uso")
    })
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRequestDTO dto) {
        service.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Permite que um usuário registrado realize o login.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas")
    })
    public String login(@Valid @RequestBody UserLoginDTO dto){
        return service.login(dto);
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verificar email", description = "Envia um código de verificação para o email fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Código de verificação enviado"),
            @ApiResponse(responseCode = "400", description = "Email inválido")
    })
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody String email){
        service.verifyEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body("Código de verificação enviado.");
    }

    @PostMapping("/verify-email/{email}/{otp}")
    @Operation(summary = "Verificar código de verificação do email", description = "Verifica o código OTP enviado para o email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email verificado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Código OTP inválido"),
            @ApiResponse(responseCode = "404", description = "Email não encontrado")
    })
    public ResponseEntity<String> verifyEmailOtp(@PathVariable String email, @PathVariable int otp){
        service.verifyEmailOtp(email, otp);
        return ResponseEntity.status(HttpStatus.OK).body("Email verificado!");
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários registrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        List<UserResponseDTO> users = service.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        UserResponseDTO user = service.findUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Atualizar informações do usuário", description = "Atualiza as informações de um usuário existente, exceto senha, email e role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<UserResponseDTO> updateUserById(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO updatedUser = service.updateUserById(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        String message = service.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
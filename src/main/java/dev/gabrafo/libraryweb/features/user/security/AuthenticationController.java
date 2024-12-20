package dev.gabrafo.libraryweb.features.user.security;

import dev.gabrafo.libraryweb.features.user.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthenticationController(AuthenticationService service, UserRepository repository, PasswordEncoder encoder) {
        this.service = service;
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public String login(UserLoginDTO request){
        var user = repository.findByEmail(request.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(request, encoder)){
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        UserAuthenticated userAuthenticated = new UserAuthenticated((user.get()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userAuthenticated, request.password());

        return service.login(authentication);
    }
}
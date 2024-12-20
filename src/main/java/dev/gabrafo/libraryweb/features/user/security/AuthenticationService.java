package dev.gabrafo.libraryweb.features.user.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private  final JwtService jwtService;

    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String login(Authentication authentication){
        return jwtService.generateToken(authentication);
    }
}

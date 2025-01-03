package dev.gabrafo.libraryweb.features.user;

import dev.gabrafo.libraryweb.errors.exceptions.ExistentEmailException;
import dev.gabrafo.libraryweb.errors.exceptions.NotFoundException;
import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.address.AddressService;
import dev.gabrafo.libraryweb.features.user.security.AuthenticationService;
import dev.gabrafo.libraryweb.features.user.security.JwtService;
import dev.gabrafo.libraryweb.features.user.security.UserAuthenticated;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final AuthenticationService authService;
    private final AddressService addressService;

    public UserService(UserRepository repository, UserMapper mapper, JwtService jwtService, PasswordEncoder encoder, AuthenticationService authService, AddressService addressService) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.authService = authService;
        this.addressService = addressService;
    }

    @Transactional
    public void registerUser(UserRequestDTO dto){
        if(repository.findByEmail(dto.email()).isPresent()){
            throw new ExistentEmailException("Email já em uso!");
        }

        Address address = addressService.findAddress(dto.zipCode());

        User newUser = mapper.toEntity(dto);
        newUser.setAddress(address);
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setEmailVerified(false);
        repository.save(newUser);
    }

    @Transactional
    public void verifyEmail(String email){
        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Por favor, informe um email válido."));
        String filepath = "templates/email-template.html";
        authService.confirmationEmail(user, email, filepath);
    }

    @Transactional
    public void verifyEmailOtp(String email, int otpCode){
        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Por favor, informe um email válido."));
        user.setEmailVerified(true);
        authService.verifyOtp(user, otpCode);
    }

    public String login(UserLoginDTO dto){
        var user = repository.findByEmail(dto.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(dto, encoder)){
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        UserAuthenticated userAuthenticated = new UserAuthenticated((user.get()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userAuthenticated, dto.password());
        return jwtService.generateToken(authentication);
    }

    public List<UserResponseDTO> findAllUsers(){
        List<User> users = repository.findAll();
        return users.stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }

    public UserResponseDTO findUserById(Long id){
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado!"));

        return mapper.toResponseDTO(user);
    }

    // Não permite mudar: Senha, email, empréstimos ou "role".
    @Transactional
    public UserResponseDTO updateUserById(Long id, UserRequestDTO dto){
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado! Não haverá atualização."));

        user.setName(dto.name());
        user.setBirthDate(dto.birthDate());

        Address address = addressService.findAddress(dto.zipCode());

        user.setAddress(address);

        return mapper.toResponseDTO(user);
    }

    @Transactional
    public String deleteUserById(Long id){
        User deleted = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado!"));

        String email = deleted.getEmail();

        repository.delete(deleted);
        return "Usuário com email '" + email + "' deletado!";
    }
}

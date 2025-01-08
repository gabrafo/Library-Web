package dev.gabrafo.libraryweb.features.user;

import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.errors.exceptions.AlreadyVerifiedException;
import dev.gabrafo.libraryweb.errors.exceptions.ExistentEmailException;
import dev.gabrafo.libraryweb.errors.exceptions.InvalidEntryException;
import dev.gabrafo.libraryweb.errors.exceptions.NotFoundException;
import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.address.AddressService;
import dev.gabrafo.libraryweb.features.user.security.UserAuthenticated;
import dev.gabrafo.libraryweb.features.user.security.otp.AuthenticationService;
import dev.gabrafo.libraryweb.infra.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
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
    public UserResponseDTO registerUser(UserRequestDTO dto){
        if(repository.findByEmail(dto.email()).isPresent()){
            throw new ExistentEmailException("Email já em uso!");
        }

        LocalDate birthDate = dto.birthDate();
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();

        if (age < 18) {
            throw new InvalidEntryException("O usuário deve ter pelo menos 18 anos.");
        }

        Address address = addressService.findAddress(dto.zipCode());

        User newUser = mapper.toEntity(dto);

        newUser.setAddress(address);
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setEmailVerified(false);
        repository.save(newUser);

        return new UserResponseDTO(newUser);
    }

    @Transactional
    public void verifyEmail(String email){
        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Por favor, informe um email válido."));
        if(user.isEmailVerified()) throw new AlreadyVerifiedException("Email já verificado.");
        String filepath = "templates/email-template.html";
        authService.confirmationEmail(user, email, filepath);
    }

    @Transactional
    public void verifyEmailOtp(String email, int otpCode){
        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Por favor, informe um email válido."));
        if(user.isEmailVerified()) throw new AlreadyVerifiedException("Email já verificado.");
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

    public boolean isUserIdOwner(Long userId, String email) {
        return repository.findById(userId)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }

    public boolean isUserEmailOwner(String email, String authEmail) {
        return repository.findByEmail(email)
                .map(user -> user.getEmail().equals(authEmail))
                .orElse(false);
    }

    public boolean isAdmin(String email){
        return repository.findByEmail(email)
                .map(user -> user.getRole().equals(Role.ADMIN))
                .orElse(false);
    }
}

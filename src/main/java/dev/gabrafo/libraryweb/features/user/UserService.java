package dev.gabrafo.libraryweb.features.user;

import dev.gabrafo.libraryweb.errors.exceptions.ExistentEmailException;
import dev.gabrafo.libraryweb.features.user.security.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Transactional
    public void registerUser(UserDTO dto){
        if(repository.findByEmail(dto.email()).isPresent()){
            throw new ExistentEmailException("Email já em uso!");
        }

        User newUser = mapper.toEntity(dto);

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        repository.save(newUser);
    }

    public List<UserDTO.UserResponse> findAllUsers(){
        List<User> users = repository.findAll();
        return users.stream().map(UserDTO.UserResponse::new).collect(Collectors.toList());
    }


}

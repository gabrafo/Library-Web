package dev.gabrafo.libraryweb.domain;

import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.errors.exceptions.ExistentEmailException;
import dev.gabrafo.libraryweb.errors.exceptions.InvalidEntryException;
import dev.gabrafo.libraryweb.features.address.AddressService;
import dev.gabrafo.libraryweb.features.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static dev.gabrafo.libraryweb.common.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressService addressService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void createUser_WithValidData_ReturnsUser(){
        when(userRepository.save(AUTHENTICATED_USER)).thenReturn(AUTHENTICATED_USER);
        when(addressService.findAddress(AUTHENTICATED_USER.getAddress().getZipCode())).thenReturn(ADDRESS);
        when(userMapper.fromRequestToEntity(AUTHENTICATED_USER_REQUEST_DTO)).thenReturn(AUTHENTICATED_USER);
        when(passwordEncoder.encode(AUTHENTICATED_USER.getPassword())).thenReturn("password");

        UserResponseDTO sut = userService.registerUser(AUTHENTICATED_USER_REQUEST_DTO);

        assertThat(sut.email()).isEqualTo(AUTHENTICATED_USER.getEmail());
        assertThat(sut.address()).isEqualTo(AUTHENTICATED_USER.getAddress());
        assertThat(sut.birthDate()).isEqualTo(AUTHENTICATED_USER.getBirthDate());
    }

    @Test
    public void createUser_IsUnderage_ThrowsException(){
        UserRequestDTO request = new UserRequestDTO(
                "Authenticated",
                "authenticated@email.com",
                new BCryptPasswordEncoder().encode("authenticated123"),
                Role.AUTHENTICATED.toString(),
                LocalDate.now(),
                ADDRESS.getZipCode()
        );

        assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(InvalidEntryException.class)
                .hasMessage("O usuário deve ter pelo menos 18 anos.");
    }

    @Test
    public void createUser_WithExistingEmail_ThrowsException(){
        when(userRepository.findByEmail(AUTHENTICATED_USER_REQUEST_DTO.email())).thenReturn(Optional.of(AUTHENTICATED_USER));
        assertThatThrownBy(() -> userService.registerUser(AUTHENTICATED_USER_REQUEST_DTO)).isInstanceOf(ExistentEmailException.class)
                .hasMessage("Email já em uso!");
    }

    @Test // Existem muitos outros casos de erro envolvendo Address, mas irei tratar apenas esse no momento!
    public void createUser_WithInvalidAddress_ThrowsException(){
        when(addressService.findAddress(AUTHENTICATED_USER_REQUEST_DTO.zipCode())).thenThrow(new InvalidEntryException("CEP não encontrado"));

        assertThatThrownBy(() -> userService.registerUser(AUTHENTICATED_USER_REQUEST_DTO))
                .isInstanceOf(InvalidEntryException.class)
                .hasMessage("CEP não encontrado");
    }
}

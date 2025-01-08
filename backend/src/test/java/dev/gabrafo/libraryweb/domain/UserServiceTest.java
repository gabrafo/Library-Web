package dev.gabrafo.libraryweb.domain;

import dev.gabrafo.libraryweb.features.address.AddressService;
import dev.gabrafo.libraryweb.features.user.UserMapper;
import dev.gabrafo.libraryweb.features.user.UserRepository;
import dev.gabrafo.libraryweb.features.user.UserResponseDTO;
import dev.gabrafo.libraryweb.features.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static dev.gabrafo.libraryweb.common.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    public void createUser_withValidData_returnsUser(){
        when(userRepository.save(AUTHENTICATED_USER)).thenReturn(AUTHENTICATED_USER);
        when(addressService.findAddress(AUTHENTICATED_USER.getAddress().getZipCode())).thenReturn(ADDRESS);
        when(userMapper.toEntity(AUTHENTICATED_USER_REQUEST_DTO)).thenReturn(AUTHENTICATED_USER);
        when(passwordEncoder.encode(AUTHENTICATED_USER.getPassword())).thenReturn("password");

        UserResponseDTO sut = userService.registerUser(AUTHENTICATED_USER_REQUEST_DTO);

        assertThat(sut.email()).isEqualTo(AUTHENTICATED_USER.getEmail());
        assertThat(sut.address()).isEqualTo(AUTHENTICATED_USER.getAddress());
        assertThat(sut.birthDate()).isEqualTo(AUTHENTICATED_USER.getBirthDate());
    }
}

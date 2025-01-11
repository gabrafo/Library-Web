package dev.gabrafo.libraryweb.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabrafo.libraryweb.errors.exceptions.ExistentEmailException;
import dev.gabrafo.libraryweb.errors.handlers.GeneralExceptionHandler;
import dev.gabrafo.libraryweb.features.address.AddressService;
import dev.gabrafo.libraryweb.features.user.UserController;
import dev.gabrafo.libraryweb.features.user.UserRepository;
import dev.gabrafo.libraryweb.features.user.UserRequestDTO;
import dev.gabrafo.libraryweb.features.user.UserService;
import dev.gabrafo.libraryweb.infra.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static dev.gabrafo.libraryweb.common.TestUtils.AUTHENTICATED_USER_REQUEST_DTO;
import static dev.gabrafo.libraryweb.common.TestUtils.AUTHENTICATED_USER_RESPONSE_DTO;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {SecurityConfig.class, UserController.class, GeneralExceptionHandler.class})
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddressService addressService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createUser_WithValidData_ReturnsCreated() throws Exception {
        when(userService.registerUser(AUTHENTICATED_USER_REQUEST_DTO)).thenReturn(AUTHENTICATED_USER_RESPONSE_DTO);

        mockMvc.perform(post("/user/register")
                        .content(objectMapper.writeValueAsString(AUTHENTICATED_USER_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(nullValue()))
                .andExpect(jsonPath("$.name").value("Authenticated"))
                .andExpect(jsonPath("$.email").value("authenticated@email.com"))
                .andExpect(jsonPath("$.role").value("AUTHENTICATED"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.address.zipCode").value("01310-200"))
                .andExpect(jsonPath("$.address.street").value("Avenida Paulista"))
                .andExpect(jsonPath("$.address.neighbourhood").value("Bela Vista"))
                .andExpect(jsonPath("$.address.city").value("São Paulo"))
                .andExpect(jsonPath("$.address.federalUnit").value("SP"))
                .andExpect(jsonPath("$.borrowedBooks").isEmpty());

    }

    @Test
    public void createUser_WithInvalidData_ReturnsBadRequest() throws Exception {
        UserRequestDTO emptyRequest = new UserRequestDTO(
                "",
                "",
                "",
                "",
                LocalDate.now(),
                ""
        );

        mockMvc.perform(post("/user/register")
                .content(objectMapper.writeValueAsString(emptyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_WithExistingEmail_ReturnsBadRequest() throws Exception {
        when(userService.registerUser(any()))
                .thenThrow(new ExistentEmailException("Email já em uso!"));;

        mockMvc.perform(post("/user/register")
                        .content(objectMapper.writeValueAsString(AUTHENTICATED_USER_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("Email já em uso!"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}

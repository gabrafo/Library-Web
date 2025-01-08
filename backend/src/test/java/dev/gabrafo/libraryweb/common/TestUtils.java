package dev.gabrafo.libraryweb.common;

import dev.gabrafo.libraryweb.enums.FederalUnit;
import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.book.Book;
import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.features.user.UserRequestDTO;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static User createAuthenticatedUser(TestEntityManager testEntityManager) {
        Address savedAddress = testEntityManager.persist(ADDRESS);
        return new User(
                "Authenticated",
                "authenticated@email.com",
                new BCryptPasswordEncoder().encode("authenticated123"),
                Role.AUTHENTICATED,
                LocalDate.of(1990, 1, 1),
                savedAddress,
                new ArrayList<>(),
                true
        );
    }

    public static final Address ADDRESS =
            new Address(
                FederalUnit.SP,
                "São Paulo",
                "Bela Vista",
                "Avenida Paulista",
                "01310-200"
        );

    public static final User AUTHENTICATED_USER =
            new User(
                "Authenticated",
                "authenticated@email.com",
                new BCryptPasswordEncoder().encode("authenticated123"),
                Role.AUTHENTICATED,
                LocalDate.of(1990, 1, 1),
                ADDRESS,
                new ArrayList<>(),
                true
        );

    public static final UserRequestDTO AUTHENTICATED_USER_REQUEST_DTO =
            new UserRequestDTO(
                    "Authenticated",
                    "authenticated@email.com",
                    new BCryptPasswordEncoder().encode("authenticated123"),
                    Role.AUTHENTICATED.toString(),
                    LocalDate.of(1990, 1, 1),
                    ADDRESS.getZipCode()
            );

    public static final Book AVAILABLE_BOOK = new Book(
            "9780134494166",
            "Clean Architecture",
            List.of("Robert C. Martin"),
            "Prentice Hall",
            LocalDate.of(2017, 9, 1),
            5,
            null);
}
package dev.gabrafo.libraryweb.common;

import dev.gabrafo.libraryweb.enums.FederalUnit;
import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.features.address.Address;
import dev.gabrafo.libraryweb.features.book.Book;
import dev.gabrafo.libraryweb.features.user.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Address createAddress(TestEntityManager testEntityManager) {
        return testEntityManager.persist(new Address(
                FederalUnit.SP,
                "São Paulo",
                "Bela Vista",
                "Avenida Paulista",
                "01310-200"
        ));
    }

    public static User createAuthenticatedUser(TestEntityManager testEntityManager) {
        Address savedAddress = testEntityManager.persist(new Address(
                FederalUnit.SP,
                "São Paulo",
                "Bela Vista",
                "Avenida Paulista",
                "01310-200"
        ));
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

    public static User createAdmindUser(TestEntityManager testEntityManager) {
        Address savedAddress = createAddress(testEntityManager);
        return new User(
                "Admin",
                "admin@email.com",
                passwordEncoder.encode("admin123"),
                Role.ADMIN,
                LocalDate.of(1985, 12, 31),
                savedAddress,
                new ArrayList<>(),
                true
        );
    }

    public static final Book AVAILABLE_BOOK = new Book(
            "9780134494166",
            "Clean Architecture",
            List.of("Robert C. Martin"),
            "Prentice Hall",
            LocalDate.of(2017, 9, 1),
            5,
            null);
}
package dev.gabrafo.libraryweb.domain;

import dev.gabrafo.libraryweb.common.TestUtils;
import dev.gabrafo.libraryweb.enums.Role;
import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.features.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Collections;

import static dev.gabrafo.libraryweb.common.TestUtils.ADDRESS;
import static dev.gabrafo.libraryweb.common.TestUtils.AUTHENTICATED_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void setUp() {
        testEntityManager.persistAndFlush(ADDRESS);
    }

    @AfterEach
    public void afterEach() {
        ADDRESS.setAddressId(null);
        testEntityManager.clear();
    }

    @Test
    void createUser_withValidData_ReturnsUser() {
        User user = TestUtils.createAuthenticatedUser(testEntityManager);
        User savedUser = userRepository.save(user);
        User sut = testEntityManager.find(User.class, savedUser.getUserId());
        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(user);
    }

    @Test
    void createUser_withNullRole_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "password123", null, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withNullName_ThrowsException() {
        User invalidUser = new User(null, "valid@example.com", "password123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withEmptyName_ThrowsException() {
        User invalidUser = new User("", "valid@example.com", "password123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withNullEmail_ThrowsException() {
        User invalidUser = new User("Valid Name", null, "password123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withEmptyEmail_ThrowsException() {
        User invalidUser = new User("Valid Name", "", "password123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withNullBirthDate_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "password123", Role.AUTHENTICATED, null,
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withFutureBirthDate_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "password123", Role.AUTHENTICATED,
                LocalDate.now().plusDays(1),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withNullPassword_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", null, Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withEmptyPassword_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withNullAddress_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "password123", Role.AUTHENTICATED, LocalDate.now(),
                null, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_withExistingEmail_ThrowsException() {
        User user = testEntityManager.persistFlushFind(AUTHENTICATED_USER);
        testEntityManager.detach(user);
        user.setUserId(null);
        assertThatThrownBy(() -> userRepository.save(user)).isInstanceOf(RuntimeException.class);
    }
}
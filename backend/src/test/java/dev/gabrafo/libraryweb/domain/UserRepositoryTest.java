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
import java.util.List;
import java.util.Optional;

import static dev.gabrafo.libraryweb.common.TestUtils.*;
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
        ADMIN_USER.setUserId(null);
        AUTHENTICATED_USER.setUserId(null);
        ADDRESS.setAddressId(null);
    }

    @Test
    void createUser_WithValidData_ReturnsUser() {
        User user = TestUtils.createAuthenticatedUser(testEntityManager);
        User savedUser = userRepository.save(user);
        User sut = testEntityManager.find(User.class, savedUser.getUserId());
        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(user);
    }

    @Test
    void createUser_WithNullRole_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "passWord123", null, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithNullName_ThrowsException() {
        User invalidUser = new User(null, "valid@example.com", "passWord123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithEmptyName_ThrowsException() {
        User invalidUser = new User("", "valid@example.com", "passWord123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithNullEmail_ThrowsException() {
        User invalidUser = new User("Valid Name", null, "passWord123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithEmptyEmail_ThrowsException() {
        User invalidUser = new User("Valid Name", "", "passWord123", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithNullBirthDate_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "passWord123", Role.AUTHENTICATED, null,
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithFutureBirthDate_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "passWord123", Role.AUTHENTICATED,
                LocalDate.now().plusDays(1),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithNullPassWord_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", null, Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithEmptyPassWord_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "", Role.AUTHENTICATED, LocalDate.now(),
                ADDRESS, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithNullAddress_ThrowsException() {
        User invalidUser = new User("Valid Name", "valid@example.com", "passWord123", Role.AUTHENTICATED, LocalDate.now(),
                null, Collections.emptyList(), false);

        assertThatThrownBy(() ->
                userRepository.save(invalidUser)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_WithExistingEmail_ThrowsException() {
        User user = testEntityManager.persistFlushFind(AUTHENTICATED_USER);
        testEntityManager.detach(user);
        user.setUserId(null);
        assertThatThrownBy(() -> userRepository.save(user)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getUserById_WithValidId_ReturnsUser(){
        User user = testEntityManager.persistFlushFind(AUTHENTICATED_USER);
        Optional<User> sut = userRepository.findById(user.getUserId());
        assertThat(sut).isPresent();
        assertThat(sut).isEqualTo(Optional.of(user));
    }

    @Test
    public void getUserById_WithInvalidId_ReturnsEmpty() {
        Optional<User> sut = userRepository.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getAllUsers_ReturnsUsers(){
        testEntityManager.persistAndFlush(AUTHENTICATED_USER);
        testEntityManager.persistAndFlush(ADMIN_USER);
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getName()).isEqualTo(AUTHENTICATED_USER.getName());
        assertThat(users.get(1).getEmail()).isEqualTo(ADMIN_USER.getEmail());
    }

    @Test
    public void getAllUsers_ReturnsEmptyList(){
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void removeUser_WithValidId_RemovesFromDb(){

        User authenticatedUser = testEntityManager.persistAndFlush(AUTHENTICATED_USER);;

        userRepository.deleteById(AUTHENTICATED_USER.getUserId());

        User removedUser = testEntityManager.find(User.class, authenticatedUser.getUserId());
        assertThat(removedUser).isNull();
    }

    @Test
    public void removeUser_WithInvalidId_DoesNotChangeDb(){

        assertThat(testEntityManager.find(User.class, 3L)).isNull();

        testEntityManager.persistAndFlush(AUTHENTICATED_USER);
        testEntityManager.persistAndFlush(ADMIN_USER);

        userRepository.deleteById(3L);

        assertThat(testEntityManager.find(User.class, 1L)).isInstanceOf(User.class);
        assertThat(testEntityManager.find(User.class, 2L)).isInstanceOf(User.class);
        assertThat(testEntityManager.find(User.class, 3L)).isNull();
    }
}
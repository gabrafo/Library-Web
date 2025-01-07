package dev.gabrafo.libraryweb.domain;

import dev.gabrafo.libraryweb.common.TestUtils;
import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.features.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach() {
        testEntityManager.getEntityManager().createNativeQuery("DELETE FROM tb_user").executeUpdate();
        testEntityManager.getEntityManager().createNativeQuery("DELETE FROM tb_address").executeUpdate();
    }

    @Test
    void createUser_withValidData_ReturnsUser(){
        User user = TestUtils.createAuthenticatedUser(testEntityManager);
        User savedUser = userRepository.save(user);
        User sut = testEntityManager.find(User.class, savedUser.getUserId());
        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(user);
    }
}

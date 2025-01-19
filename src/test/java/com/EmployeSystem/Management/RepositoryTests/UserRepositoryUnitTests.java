package com.EmployeSystem.Management.RepositoryTests;



import com.EmployeSystem.Management.model.User;
import com.EmployeSystem.Management.repository.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryUnitTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test 1: Save User Test")
    @Order(1)
    @Rollback(value = false)
    public void saveUserTest() {
        // Action
        User user = new User();
        user.setUsername("Youmna");
        user.setPassword("123456");
        user.setRole("HR");
        user.setDepartment("Human Ressources");

        userRepository.save(user);

        // Verify
        System.out.println(user);
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test 2: Get User By ID Test")
    @Order(2)
    public void getUserByIdTest() {
        // Action
        User user = userRepository.findById(1L).get();

        // Verify
        System.out.println(user);
        Assertions.assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test 3: Get List of Users Test")
    @Order(3)
    public void getListOfUsersTest() {
        // Action
        List<User> users = userRepository.findAll();

        // Verify
        System.out.println(users);
        Assertions.assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test 4: Update User Test")
    @Order(4)
    @Rollback(value = false)
    public void updateUserTest() {
        // Action
        User user = userRepository.findById(1L).get();
        user.setUsername("Youmna123");
        User updatedUser = userRepository.save(user);

        // Verify
        System.out.println(updatedUser);
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo("Youmna123");
    }

    @Test
    @DisplayName("Test 5: Delete User Test")
    @Order(5)
    @Rollback(value = false)
    public void deleteUserTest() {
        // Action
        userRepository.deleteById(1L);
        Optional<User> userOptional = userRepository.findById(1L);

        // Verify
        Assertions.assertThat(userOptional).isEmpty();
    }
}


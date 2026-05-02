package com.samialsohan.taskflow.repository;


import com.samialsohan.taskflow.BaseIntegrationTest;
import com.samialsohan.taskflow.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save a user and generate an ID")
    void saveUser() {
        User user = new User("Alice", "alice@example.com");

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Alice");
        assertThat(saved.getEmail()).isEqualTo("alice@example.com");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should find all users")
    void findAllUsers() {
        userRepository.save(new User("Alice", "alice@example.com"));
        userRepository.save(new User("Bob", "bob@example.com"));

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("Should find user by ID")
    void findById() {
        User saved = userRepository.save(new User("Alice", "alice@example.com"));

        User found = userRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void findByIdNotFound() {
        var result = userRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should delete a user")
    void deleteUser() {
        User saved = userRepository.save(new User("Alice", "alice@example.com"));

        userRepository.deleteById(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }
}
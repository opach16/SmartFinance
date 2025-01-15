package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTestSuite {

    private User user;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User("testUsername", "testEmail", "testPassword");
        userRepository.save(user);
    }

    @Test
    void findAll() {
        //given
        //when
        List<User> users = userRepository.findAll();
        //then
        assertEquals(1, users.size());
        assertEquals("testUsername", users.getFirst().getUsername());
        assertEquals("testEmail", users.getFirst().getEmail());
        assertEquals("testPassword", users.getFirst().getPassword());
    }

    @Test
    void findByUsername() {
        //given
        //when
        Optional<User> fetchedUser = userRepository.findByUsername(user.getUsername());
        //then
        assertTrue(fetchedUser.isPresent());
        assertEquals(user.getUsername(), fetchedUser.get().getUsername());
    }
}
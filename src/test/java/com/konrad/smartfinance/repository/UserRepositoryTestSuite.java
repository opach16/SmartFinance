package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTestSuite {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldAddUser() {
        //given
        User user = new User("testUsername", "testMail", "testPassword");
        //when
        userRepository.save(user);
        //then
        Optional<User> retrievedUser = userRepository.findById(user.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals(user.getId(), retrievedUser.get().getId());
        assertEquals(user.getUsername(), retrievedUser.get().getUsername());
        assertEquals(user.getPassword(), retrievedUser.get().getPassword());
        assertEquals(user.getCreatedAt(), retrievedUser.get().getCreatedAt());
    }

    @Test
    void shouldUpdateUser() {
        //given
        User user = new User("testUsername", "testMail", "testPassword");
        userRepository.save(user);
        //when
        user.setUsername("updatedUsername");
        user.setEmail("updatedEmail");
        user.setPassword("updatedPassword");
        userRepository.save(user);
        //then
        Optional<User> retrievedUser = userRepository.findById(user.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals(user.getId(), retrievedUser.get().getId());
        assertEquals(user.getUsername(), retrievedUser.get().getUsername());
        assertEquals(user.getEmail(), retrievedUser.get().getEmail());
        assertEquals(user.getPassword(), retrievedUser.get().getPassword());
        assertEquals(user.getCreatedAt(), retrievedUser.get().getCreatedAt());
        assertNotEquals(user.getUpdatedAt(), retrievedUser.get().getUpdatedAt());
    }

    @Test
    void shouldFetchAllUsers() {
        //given
        User user1 = new User("testUsername1", "testMail1", "testPassword1");
        User user2 = new User("testUsername2", "testMail2", "testPassword2");
        userRepository.save(user1);
        userRepository.save(user2);
        //when
        List<User> retrievedUsers = userRepository.findAll();
        //then
        assertEquals(2, retrievedUsers.size());
    }

    @Test
    void shouldDeleteEmptyUser() {
        //given
        User user = new User("testUsername", "testMail", "testPassword");
        userRepository.save(user);
        //when
        userRepository.deleteById(user.getId());
        //then
        Optional<User> retrievedUser = userRepository.findById(user.getId());
        assertTrue(retrievedUser.isEmpty());
    }
}

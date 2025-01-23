package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.domain.model.UserPrincipal;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTestSuite {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        //given
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        // when
        UserPrincipal userDetails = (UserPrincipal) myUserDetailsService.loadUserByUsername(user.getUsername());

        // then
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        //given
        when(userRepository.findByUsername("testUsername")).thenReturn(Optional.empty());
        //when & then
        assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername("testUsername"));
        verify(userRepository, times(1)).findByUsername("testUsername");
    }
}
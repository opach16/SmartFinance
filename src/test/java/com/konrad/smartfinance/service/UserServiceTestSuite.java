package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTestSuite {

    private User user1;
    private User user2;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private BCryptPasswordEncoder bCrypt;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user1 = User.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .build();
        user2 = User.builder()
                .id(2L)
                .username("testUsername2")
                .email("testEmail2")
                .password("testPassword2")
                .build();
    }

    @Test
    void shouldGetAllUsers() {
        //given
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        //when
        List<User> fetchedUsers = userService.getAllUsers();
        //then
        assertNotNull(fetchedUsers);
        assertEquals(users.size(), fetchedUsers.size());
        assertEquals(users.getFirst().getId(), fetchedUsers.getFirst().getId());
        assertEquals(users.getLast().getId(), fetchedUsers.getLast().getId());
    }

    @Test
    void shouldGetUserById() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        //when
        User fetchedUser = userService.getUserById(1L);
        //then
        assertNotNull(fetchedUser);
        assertEquals(user1.getId(), fetchedUser.getId());
        assertEquals(user1.getUsername(), fetchedUser.getUsername());
        assertEquals(user1.getEmail(), fetchedUser.getEmail());
        assertEquals(user1.getPassword(), fetchedUser.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetUserById() {
        //given
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> userService.getUserById(3L));
    }

    @Test
    void shouldGetUserByUsername() throws UserException {
        //given
        when(userRepository.findByUsername("testUsername1")).thenReturn(Optional.of(user1));
        //when
        User fetchedUser = userService.getUserByUsername("testUsername1");
        //then
        assertNotNull(fetchedUser);
        assertEquals(user1.getId(), fetchedUser.getId());
        assertEquals(user1.getUsername(), fetchedUser.getUsername());
        assertEquals(user1.getEmail(), fetchedUser.getEmail());
        assertEquals(user1.getPassword(), fetchedUser.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsernameOnGetUserByUsername() {
        //given
        when(userRepository.findByUsername("testUsername3")).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> userService.getUserByUsername("testUsername3"));
    }

    @Test
    void shouldAddUser() throws CurrencyExeption, UserException {
        //given
        Currency currency = Currency.builder()
                .id(1L)
                .symbol("testSymbol")
                .price(BigDecimal.TEN)
                .build();
        Account account = Account.builder()
                .id(1L)
                .user(user1)
                .mainBalance(BigDecimal.TEN)
                .mainCurrency(currency)
                .build();
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.empty());
        when(currencyService.getCurrencyBySymbol("testSymbol")).thenReturn(currency);
        when(userRepository.save(user1)).thenReturn(user1);
        when(bCrypt.encode(user1.getPassword())).thenReturn("encodedPassword");
        when(accountService.createAccount(any(Account.class))).thenReturn(account);
        //when
        User savedUser = userService.addUser(user1, "testSymbol", BigDecimal.TEN);
        //then
        assertNotNull(savedUser);
        assertEquals("testUsername1", savedUser.getUsername());
        assertNotNull(savedUser.getAccount());
        assertEquals(account.getId(), savedUser.getAccount().getId());
        assertEquals(currency, savedUser.getAccount().getMainCurrency());
        assertEquals(BigDecimal.TEN, savedUser.getAccount().getMainBalance());
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(2)).save(user1);
        verify(accountService, times(1)).createAccount(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExistsOnAddUser() {
        //given
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        //when & then
        assertThrows(UserException.class, () -> userService.addUser(user1, "testSymbol", BigDecimal.TEN));
        verify(userRepository, never()).save(any(User.class));
        verify(accountService, never()).createAccount(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotFoundOnAddUser() throws CurrencyExeption {
        //given
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.empty());
        when(currencyService.getCurrencyBySymbol("invalidSymbol"))
                .thenThrow(new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        //when & then
        assertThrows(CurrencyExeption.class, () -> userService.addUser(user1, "invalidSymbol", BigDecimal.TEN));
        verify(userRepository, never()).save(any(User.class));
        verify(accountService, never()).createAccount(any(Account.class));
    }

    @Test
    void shouldUpdateUser() throws UserException {
        //given
        User updatedUser = User.builder()
                .id(1L)
                .username("updatedUsername")
                .email("updatedEmail")
                .password("updatedPassword")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(updatedUser);

        //when
        User fetchedUser = userService.updateUser(1L, updatedUser);
        //then
        assertNotNull(fetchedUser);
        assertEquals(user1.getId(), fetchedUser.getId());
        assertEquals(updatedUser.getUsername(), fetchedUser.getUsername());
        assertEquals(updatedUser.getEmail(), fetchedUser.getEmail());
        assertEquals(updatedUser.getPassword(), fetchedUser.getPassword());
        assertEquals(user1.getCreatedAt(), fetchedUser.getCreatedAt());
        assertNotEquals(user1.getUpdatedAt(), fetchedUser.getUpdatedAt());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdateUser() {
        //given
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> userService.updateUser(2L, user2));
    }

    @Test
    void shouldDeleteUser() throws UserException {
        //given
        User deletedUser = User.builder()
                .id(1L)
                .username("deletedUsername")
                .email("deletedEmail")
                .password("deletedPassword")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(deletedUser);

        //when
        User fetchedUser = userService.updateUser(1L, deletedUser);
        //then
        assertNotNull(fetchedUser);
        assertEquals(user1.getId(), fetchedUser.getId());
        assertEquals(deletedUser.getUsername(), fetchedUser.getUsername());
        assertEquals(deletedUser.getEmail(), fetchedUser.getEmail());
        assertEquals(deletedUser.getPassword(), fetchedUser.getPassword());
        assertEquals(user1.getCreatedAt(), fetchedUser.getCreatedAt());
        assertNotEquals(user1.getUpdatedAt(), fetchedUser.getUpdatedAt());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDeleteUser() {
        //given
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> userService.deleteUser(2L));
    }
}
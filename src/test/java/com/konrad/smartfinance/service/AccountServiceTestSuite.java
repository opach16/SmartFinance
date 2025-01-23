package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.AccountRepository;
import com.konrad.smartfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTestSuite {

    private User user;
    private Currency currency;
    private Account account;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .build();
        currency = new Currency(1L, "EUR", BigDecimal.ONE, LocalDateTime.now(), LocalDateTime.now());
        account = Account.builder()
                .id(1L)
                .user(user)
                .mainCurrency(currency)
                .mainBalance(BigDecimal.valueOf(1000))
                .assetsBalance(BigDecimal.valueOf(500))
                .build();
        user.setAccount(account);
    }

    @Test
    void shouldCreateAccount() {
        //given
        when(accountRepository.save(account)).thenReturn(account);
        //when
        Account savedAccount = accountService.createAccount(account);
        //then
        assertEquals(account.getId(), savedAccount.getId());
    }

    @Test
    void shouldGetAccountByUserId() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when & then
        Account fetchedAccount = accountService.getAccountByUserId(1L);
        assertEquals(user.getAccount(), fetchedAccount);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        //given
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> accountService.getAccountByUserId(2L));
    }

    @Test
    void shouldGetMainBalance() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        BigDecimal mainBalanceByUserId = accountService.getMainBalanceByUserId(user.getId());
        //then
        assertEquals(user.getAccount().getMainBalance(), mainBalanceByUserId);
    }

    @Test
    void shouldGetAssetsBalance() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        BigDecimal assetsBalanceByUserId = accountService.getAssetsBalanceByUserId(user.getId());
        //then
        assertEquals(user.getAccount().getAssetsBalance(), assetsBalanceByUserId);
    }

    @Test
    void shouldGetTotalBalance() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        BigDecimal totalBalanceByUserId = accountService.getTotalBalanceByUserId(user.getId());
        //then
        assertEquals(user.getAccount().getMainBalance().add(user.getAccount().getAssetsBalance()), totalBalanceByUserId);
    }
}
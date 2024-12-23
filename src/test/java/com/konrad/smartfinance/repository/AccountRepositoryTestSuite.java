package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountRepositoryTestSuite {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void shouldAddAccount() {
        //given
        User user = new User("testUsername", "testEmail", "testPassword");
        userRepository.save(user);
        Currency currency = new Currency("USD", "Dollar", BigDecimal.TEN);
        currencyRepository.save(currency);
        Account account = new Account(user, currency);
        //when
        accountRepository.save(account);
        //then
        Optional<Account> retrievedAccount = accountRepository.findById(account.getId());
        assertTrue(retrievedAccount.isPresent());
        assertEquals(account.getId(), retrievedAccount.get().getId());
        assertEquals(account.getUser().getId(), retrievedAccount.get().getUser().getId());
        assertEquals(account.getMainCurrency().getId(), retrievedAccount.get().getMainCurrency().getId());
        assertEquals(account.getMainBalance(), retrievedAccount.get().getMainBalance());
        assertEquals(account.getTotalBalance(), retrievedAccount.get().getTotalBalance());
        assertEquals(account.getCreatedAt(), retrievedAccount.get().getCreatedAt());
        assertEquals(account.getUpdatedAt(), retrievedAccount.get().getUpdatedAt());
    }

    @Test
    void shouldUpdateAccount() {
        //given
        User user = new User("testUsername", "testEmail", "testPassword");
        userRepository.save(user);
        Currency currency1 = new Currency("USD", "Dollar", BigDecimal.TEN);
        Currency currency2 = new Currency("EUR", "Euro", BigDecimal.ONE);
        currencyRepository.save(currency1);
        currencyRepository.save(currency2);
        Account account = new Account(user, currency1);
        accountRepository.save(account);
        //when
        account.setMainCurrency(currency2);
        accountRepository.save(account);
        //then
        Optional<Account> retrievedAccount = accountRepository.findById(account.getId());
        assertTrue(retrievedAccount.isPresent());
        assertEquals(account.getId(), retrievedAccount.get().getId());
        assertEquals(account.getUser().getId(), retrievedAccount.get().getUser().getId());
        assertEquals(account.getMainCurrency().getId(), retrievedAccount.get().getMainCurrency().getId());
        assertEquals(account.getMainBalance(), retrievedAccount.get().getMainBalance());
        assertEquals(account.getTotalBalance(), retrievedAccount.get().getTotalBalance());
        assertEquals(account.getCreatedAt(), retrievedAccount.get().getCreatedAt());
        assertNotEquals(account.getUpdatedAt(), retrievedAccount.get().getUpdatedAt());
    }
}

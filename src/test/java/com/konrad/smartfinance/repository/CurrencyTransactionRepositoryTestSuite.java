package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.CurrencyTransactionType;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.domain.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CurrencyTransactionRepositoryTestSuite {

    private User user;
    private Currency currency;
    private CurrencyTransaction transaction;

    @Autowired
    CurrencyTransactionRepository currencyTransactionRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User("testUsername", "testEmail", "testPassword");
        userRepository.save(user);
        currency = new Currency("testSymbol", BigDecimal.ONE);
        currencyRepository.save(currency);
        transaction = new CurrencyTransaction(user, currency, CurrencyTransactionType.BUY,
                BigDecimal.ONE, BigDecimal.ONE, LocalDate.of(2025, 1, 1));
        currencyTransactionRepository.save(transaction);
    }

    @Test
    void findAll() {
        //given
        //when
        List<CurrencyTransaction> fetchedCurrencyTransactions = currencyTransactionRepository.findAll();
        //then
        assertEquals(1, fetchedCurrencyTransactions.size());
    }

    @Test
    void findByUserId() {
        //given
        //when
        List<CurrencyTransaction> fetchedCurrencyTransactions = currencyTransactionRepository.findByUserId(user.getId());
        //then
        assertEquals(1, fetchedCurrencyTransactions.size());
        assertEquals(transaction.getUser().getId(), fetchedCurrencyTransactions.getFirst().getUser().getId());
    }
}
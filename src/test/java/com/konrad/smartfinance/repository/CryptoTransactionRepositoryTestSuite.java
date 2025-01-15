package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
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
class CryptoTransactionRepositoryTestSuite {

    private User user;
    private Cryptocurrency crypto;
    private CryptoTransaction transaction;

    @Autowired
    CryptoTransactionRepository cryptoTransactionRepository;

    @Autowired
    CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User("testUsername", "testEmail", "testPassword");
        userRepository.save(user);
        crypto = new Cryptocurrency("testSymbol", "testName", BigDecimal.ONE);
        cryptocurrencyRepository.save(crypto);
        transaction = new CryptoTransaction(user, crypto, CryptoTransactionType.BUY,
                BigDecimal.ONE, BigDecimal.ONE, LocalDate.of(2025, 1, 1));
        cryptoTransactionRepository.save(transaction);
    }

    @Test
    void findAll() {
        //given
        //when
        List<CryptoTransaction> fetchedCryptoTransactions = cryptoTransactionRepository.findAll();
        //then
        assertEquals(1, fetchedCryptoTransactions.size());
    }

    @Test
    void findByUserId() {
        //given
        //when
        List<CryptoTransaction> fetchedCryptoTransactions = cryptoTransactionRepository.findByUserId(user.getId());
        //then
        assertEquals(1, fetchedCryptoTransactions.size());
        assertEquals(transaction.getUser().getId(), fetchedCryptoTransactions.getFirst().getUser().getId());
    }
}
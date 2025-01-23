package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Cryptocurrency;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CryptocurrencyRepositoryTestSuite {

    private Cryptocurrency crypto;

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    @BeforeEach
    void setUp() {
        cryptocurrencyRepository.deleteAll();
        crypto = new Cryptocurrency("testSymbol", "testName", BigDecimal.ONE);
        cryptocurrencyRepository.save(crypto);
    }

    @Test
    void findAll() {
        //given
        //when
        List<Cryptocurrency> fetchedCryptos = cryptocurrencyRepository.findAll();
        //then
        assertEquals(1, fetchedCryptos.size());
    }

    @Test
    void findBySymbol() {
        //given
        //when
        Optional<Cryptocurrency> fetchedCrypto = cryptocurrencyRepository.findBySymbol(crypto.getSymbol());
        //then
        assertTrue(fetchedCrypto.isPresent());
        assertEquals(crypto.getSymbol(), fetchedCrypto.get().getSymbol());
    }
}
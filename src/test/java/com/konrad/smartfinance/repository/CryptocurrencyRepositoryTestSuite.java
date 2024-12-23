package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Cryptocurrency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CryptocurrencyRepositoryTestSuite {

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    @Test
    void shouldAddCryptocurrency() {
        //given
        Cryptocurrency cryptocurrency = new Cryptocurrency("BTC", "Bitcoin", new BigDecimal("100.00"));
        //when
        cryptocurrencyRepository.save(cryptocurrency);
        //then
        Optional<Cryptocurrency> retrievedCryptocurrency = cryptocurrencyRepository.findById(cryptocurrency.getId());
        assertTrue(retrievedCryptocurrency.isPresent());
        assertEquals(cryptocurrency.getId(), retrievedCryptocurrency.get().getId());
        assertEquals(cryptocurrency.getSymbol(), retrievedCryptocurrency.get().getSymbol());
        assertEquals(cryptocurrency.getName(), retrievedCryptocurrency.get().getName());
        assertEquals(cryptocurrency.getPrice(), retrievedCryptocurrency.get().getPrice());
        assertEquals(cryptocurrency.getCreatedAt(), retrievedCryptocurrency.get().getCreatedAt());
        assertEquals(cryptocurrency.getUpdatedAt(), retrievedCryptocurrency.get().getUpdatedAt());
    }

    @Test
    void shouldUpdateCryptocurrency() {
        //given
        Cryptocurrency cryptocurrency = new Cryptocurrency("BTC", "Bitcoin", new BigDecimal("100.00"));
        cryptocurrencyRepository.save(cryptocurrency);
        //when
        cryptocurrency.setSymbol("XRP");
        cryptocurrency.setName("Ripple");
        cryptocurrency.setPrice(new BigDecimal("22.22"));
        cryptocurrencyRepository.save(cryptocurrency);
        //then
        Optional<Cryptocurrency> retrievedCryptocurrency = cryptocurrencyRepository.findById(cryptocurrency.getId());
        assertTrue(retrievedCryptocurrency.isPresent());
        assertEquals(cryptocurrency.getId(), retrievedCryptocurrency.get().getId());
        assertEquals(cryptocurrency.getSymbol(), retrievedCryptocurrency.get().getSymbol());
        assertEquals(cryptocurrency.getName(), retrievedCryptocurrency.get().getName());
        assertEquals(cryptocurrency.getPrice(), retrievedCryptocurrency.get().getPrice());
        assertEquals(cryptocurrency.getCreatedAt(), retrievedCryptocurrency.get().getCreatedAt());
        assertNotEquals(cryptocurrency.getUpdatedAt(), retrievedCryptocurrency.get().getUpdatedAt());
    }

    @Test
    void shouldDeleteCryptocurrency() {
        //given
        Cryptocurrency cryptocurrency = new Cryptocurrency("BTC", "Bitcoin", new BigDecimal("100.00"));
        cryptocurrencyRepository.save(cryptocurrency);
        //when
        cryptocurrencyRepository.deleteById(cryptocurrency.getId());
        //then
        Optional<Cryptocurrency> retrievedCryptocurrency = cryptocurrencyRepository.findById(cryptocurrency.getId());
        assertTrue(retrievedCryptocurrency.isEmpty());
    }
}

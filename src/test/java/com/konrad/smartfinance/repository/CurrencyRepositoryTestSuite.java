package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CurrencyRepositoryTestSuite {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void shouldAddCurrency() {
        //given
        Currency currency = new Currency("USD", "Dollar", new BigDecimal("100.00"));
        //when
        currencyRepository.save(currency);
        //then
        Optional<Currency> retrievedCurrency = currencyRepository.findById(currency.getId());
        assertTrue(retrievedCurrency.isPresent());
        assertEquals(currency.getId(), retrievedCurrency.get().getId());
        assertEquals(currency.getSymbol(), retrievedCurrency.get().getSymbol());
        assertEquals(currency.getName(), retrievedCurrency.get().getName());
        assertEquals(currency.getPrice(), retrievedCurrency.get().getPrice());
        assertEquals(currency.getCreatedAt(), retrievedCurrency.get().getCreatedAt());
        assertEquals(currency.getUpdatedAt(), retrievedCurrency.get().getUpdatedAt());
    }

    @Test
    void shouldUpdateCurrency() {
        //given
        Currency currency = new Currency("USD", "Dollar", new BigDecimal("100.00"));
        currencyRepository.save(currency);
        //when
        currency.setSymbol("EUR");
        currency.setName("Euro");
        currency.setPrice(new BigDecimal("99.99"));
        currencyRepository.save(currency);
        //then
        Optional<Currency> retrievedCurrency = currencyRepository.findById(currency.getId());
        assertTrue(retrievedCurrency.isPresent());
        assertEquals(currency.getId(), retrievedCurrency.get().getId());
        assertEquals(currency.getSymbol(), retrievedCurrency.get().getSymbol());
        assertEquals(currency.getName(), retrievedCurrency.get().getName());
        assertEquals(currency.getPrice(), retrievedCurrency.get().getPrice());
        assertEquals(currency.getCreatedAt(), retrievedCurrency.get().getCreatedAt());
        assertNotEquals(currency.getUpdatedAt(), retrievedCurrency.get().getUpdatedAt());
    }

    @Test
    void shouldDeleteCurrency() {
        //given
        Currency currency = new Currency("USD", "Dollar", new BigDecimal("100.00"));
        currencyRepository.save(currency);
        //when
        currencyRepository.delete(currency);
        //then
        Optional<Currency> retrievedCurrency = currencyRepository.findById(currency.getId());
        assertTrue(retrievedCurrency.isEmpty());
    }
}

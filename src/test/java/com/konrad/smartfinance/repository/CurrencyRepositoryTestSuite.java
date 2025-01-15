package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Currency;
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
class CurrencyRepositoryTestSuite {

    private Currency currency;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        currency = new Currency("testSymbol", BigDecimal.ONE);
        currencyRepository.save(currency);
    }

    @Test
    void findAll() {
        //given
        //when
        List<Currency> currencies = currencyRepository.findAll();
        //then
        assertEquals(1, currencies.size());
    }

    @Test
    void findBySymbol() {
        //given
        //when
        Optional<Currency> fetchedCurrency = currencyRepository.findBySymbol(currency.getSymbol());
        //then
        assertTrue(fetchedCurrency.isPresent());
        assertEquals(currency.getSymbol(), fetchedCurrency.get().getSymbol());
    }
}
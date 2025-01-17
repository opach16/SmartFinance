package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CurrencyDto;
import com.konrad.smartfinance.domain.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CurrencyMapperTestSuite {

    @Autowired
    private CurrencyMapper currencyMapper;

    @Test
    void shouldMapToCurrencyDto() {
        //given
        Currency currency = Currency.builder()
                .id(1L)
                .symbol("testSymbol")
                .price(BigDecimal.ONE)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        CurrencyDto mappedCurrencyDto = currencyMapper.mapToCurrencyDto(currency);
        //then
        assertNotNull(mappedCurrencyDto);
        assertEquals(currency.getId(), mappedCurrencyDto.getId());
        assertEquals(currency.getSymbol(), mappedCurrencyDto.getSymbol());
        assertEquals(currency.getPrice(), mappedCurrencyDto.getPrice());
        assertEquals(currency.getCreatedAt(), mappedCurrencyDto.getCreatedAt());
        assertEquals(currency.getUpdatedAt(), mappedCurrencyDto.getUpdatedAt());
    }

    @Test
    void shouldMapToCurrencyDtoList() {
        //given
        Currency currency1 = Currency.builder()
                .id(1L)
                .symbol("testSymbol1")
                .price(BigDecimal.ONE)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        Currency currency2 = Currency.builder()
                .id(2L)
                .symbol("testSymbol2")
                .price(BigDecimal.TWO)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        List<Currency> currencyList = List.of(currency1, currency2);
        //when
        List<CurrencyDto> currencyDtoList = currencyMapper.mapToCurrencyDtoList(currencyList);
        //then
        assertNotNull(currencyDtoList);
        assertEquals(currencyList.size(), currencyDtoList.size());
        assertEquals(currency1.getId(), currencyDtoList.getFirst().getId());
        assertEquals(currency1.getSymbol(), currencyDtoList.getFirst().getSymbol());
        assertEquals(currency2.getId(), currencyDtoList.getLast().getId());
    }
}
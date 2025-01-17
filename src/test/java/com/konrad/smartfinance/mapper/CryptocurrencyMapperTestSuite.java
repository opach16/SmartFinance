package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CryptocurrencyDto;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CryptocurrencyMapperTestSuite {

    @Autowired
    private CryptocurrencyMapper cryptocurrencyMapper;

    @Test
    void shouldMapToCryptocurrencyDto() {
        //given
        Cryptocurrency crypto = Cryptocurrency.builder()
                .id(1L)
                .symbol("testSymbol")
                .name("testName")
                .price(BigDecimal.ONE)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        CryptocurrencyDto mappedCryptoDto = cryptocurrencyMapper.mapToCryptocurrencyDto(crypto);
        //then
        assertNotNull(mappedCryptoDto);
        assertEquals(crypto.getId(), mappedCryptoDto.getId());
        assertEquals(crypto.getSymbol(), mappedCryptoDto.getSymbol());
        assertEquals(crypto.getName(), mappedCryptoDto.getName());
        assertEquals(crypto.getPrice(), mappedCryptoDto.getPrice());
        assertEquals(crypto.getCreatedAt(), mappedCryptoDto.getCreatedAt());
        assertEquals(crypto.getUpdatedAt(), mappedCryptoDto.getUpdatedAt());
    }

    @Test
    void shouldMapToCryptocurrencyDtoList() {
        //given
        Cryptocurrency crypto1 = Cryptocurrency.builder()
                .id(1L)
                .symbol("testSymbol1")
                .name("testName1")
                .price(BigDecimal.ONE)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        Cryptocurrency crypto2 = Cryptocurrency.builder()
                .id(2L)
                .symbol("testSymbol2")
                .name("testName2")
                .price(BigDecimal.TWO)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        List<Cryptocurrency> cryptoList = List.of(crypto1, crypto2);
        //when
        List<CryptocurrencyDto> cryptoDtoList = cryptocurrencyMapper.mapToCryptocurrencyDtoList(cryptoList);
        //then
        assertNotNull(cryptoDtoList);
        assertEquals(cryptoList.size(), cryptoDtoList.size());
        assertEquals(crypto1.getId(), cryptoDtoList.getFirst().getId());
        assertEquals(crypto1.getName(), cryptoDtoList.getFirst().getName());
        assertEquals(crypto1.getPrice(), cryptoDtoList.getFirst().getPrice());
        assertEquals(crypto2.getId(), cryptoDtoList.getLast().getId());
    }
}
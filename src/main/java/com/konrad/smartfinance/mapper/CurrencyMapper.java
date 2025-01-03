package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CurrencyDto;
import com.konrad.smartfinance.domain.model.Currency;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyMapper {

    public CurrencyDto mapToCurrencyDto(Currency currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .symbol(currency.getSymbol())
                .price(currency.getPrice())
                .createdAt(currency.getCreatedAt())
                .updatedAt(currency.getUpdatedAt())
                .build();
    }

    public Currency mapToCurrencyEntity(CurrencyDto currencyDto) {
        return Currency.builder()
                .symbol(currencyDto.getSymbol())
                .price(currencyDto.getPrice())
                .build();
    }

    public List<CurrencyDto> mapToCurrencyDtoList(List<Currency> currencyList) {
        return currencyList.stream()
                .map(this::mapToCurrencyDto)
                .toList();
    }

    public List<Currency> mapToCurrencyEntityList(List<CurrencyDto> currencyDtoList) {
        return currencyDtoList.stream()
                .map(this::mapToCurrencyEntity)
                .toList();
    }
}

package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CryptocurrencyDto;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptocurrencyMapper {

    public Cryptocurrency mapToCryptocurrency(CryptocurrencyDto cryptocurrencyDto) {
        return Cryptocurrency.builder()
                .symbol(cryptocurrencyDto.getSymbol())
                .name(cryptocurrencyDto.getName())
                .price(cryptocurrencyDto.getPrice())
                .build();
    }

    public CryptocurrencyDto mapToCryptocurrencyDto(Cryptocurrency cryptocurrency) {
        return CryptocurrencyDto.builder()
                .id(cryptocurrency.getId())
                .symbol(cryptocurrency.getSymbol())
                .name(cryptocurrency.getName())
                .price(cryptocurrency.getPrice())
                .createdAt(cryptocurrency.getCreatedAt())
                .updatedAt(cryptocurrency.getUpdatedAt())
                .build();
    }

    public List<Cryptocurrency> mapToCryptocurrencyList(List<CryptocurrencyDto> cryptocurrencyDtoList) {
        return cryptocurrencyDtoList.stream()
                .map(this::mapToCryptocurrency)
                .toList();
    }

    public List<CryptocurrencyDto> mapToCryptocurrencyDtoList(List<Cryptocurrency> cryptocurrencyList) {
        return cryptocurrencyList.stream()
                .map(this::mapToCryptocurrencyDto)
                .toList();
    }
}

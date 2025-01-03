package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoTransactionMapper {

    private final UserMapper userMapper;
    private final CryptocurrencyMapper cryptocurrencyMapper;

    public CryptoTransaction mapToCryptoTransaction(CryptoTransactionDto cryptoTransactionDto) {
        return CryptoTransaction.builder()
                .user(userMapper.mapToUserEntity(cryptoTransactionDto.getUser()))
                .cryptocurrency(cryptocurrencyMapper.mapToCryptocurrency(cryptoTransactionDto.getCryptocurrency()))
                .cryptoTransactionType(cryptoTransactionDto.getTransactionType())
                .amount(cryptoTransactionDto.getAmount())
                .price(cryptoTransactionDto.getPrice())
                .transactionDate(cryptoTransactionDto.getTransactionDate())
                .build();
    }

    public CryptoTransactionDto mapToCryptoTransactionDto(CryptoTransaction cryptoTransaction) {
        return CryptoTransactionDto.builder()
                .id(cryptoTransaction.getId())
                .user(userMapper.mapToUserDto(cryptoTransaction.getUser()))
                .cryptocurrency(cryptocurrencyMapper.mapToCryptocurrencyDto(cryptoTransaction.getCryptocurrency()))
                .transactionType(cryptoTransaction.getCryptoTransactionType())
                .amount(cryptoTransaction.getAmount())
                .price(cryptoTransaction.getPrice())
                .transactionDate(cryptoTransaction.getTransactionDate())
                .createdAt(cryptoTransaction.getCreatedAt())
                .updatedAt(cryptoTransaction.getUpdatedAt())
                .build();
    }

    public List<CryptoTransaction> mapToCryptoTransactionList(List<CryptoTransactionDto> cryptoTransactionDtoList) {
        return cryptoTransactionDtoList.stream()
                .map(this::mapToCryptoTransaction)
                .toList();
    }

    public List<CryptoTransactionDto> mapToCryptoTransactionDtoList(List<CryptoTransaction> cryptoTransactionList) {
        return cryptoTransactionList.stream()
                .map(this::mapToCryptoTransactionDto)
                .toList();
    }
}
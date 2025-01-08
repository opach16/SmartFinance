package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoTransactionMapper {

    private final UserMapper userMapper;
    private final CryptocurrencyMapper cryptocurrencyMapper;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    public CryptoTransaction mapToCryptoTransaction(CryptoTransactionDto transactionDto) {
        return CryptoTransaction.builder()
                .user(userMapper.mapToUserEntity(transactionDto.getUser()))
                .cryptocurrency(cryptocurrencyMapper.mapToCryptocurrency(transactionDto.getCryptocurrency()))
                .cryptoTransactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .price(transactionDto.getPrice())
                .transactionDate(transactionDto.getTransactionDate())
                .build();
    }

    public CryptoTransactionDto mapToCryptoTransactionDto(CryptoTransaction transaction) {
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findById(transaction.getCryptocurrency().getId()).orElseThrow();
        return CryptoTransactionDto.builder()
                .id(transaction.getId())
                .user(userMapper.mapToUserDto(transaction.getUser()))
                .cryptocurrency(cryptocurrencyMapper.mapToCryptocurrencyDto(transaction.getCryptocurrency()))
                .transactionType(transaction.getCryptoTransactionType())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionValue(transaction.getAmount().multiply(transaction.getPrice()))
                .currentValue(transaction.getAmount().multiply(cryptocurrency.getPrice()))
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .cryptocurrencySymbol(transaction.getCryptocurrency().getSymbol())
                .cryptocurrencyName(transaction.getCryptocurrency().getName())
                .build();
    }

    public List<CryptoTransaction> mapToCryptoTransactionList(List<CryptoTransactionDto> transactionDtoList) {
        return transactionDtoList.stream()
                .map(this::mapToCryptoTransaction)
                .toList();
    }

    public List<CryptoTransactionDto> mapToCryptoTransactionDtoList(List<CryptoTransaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToCryptoTransactionDto)
                .toList();
    }
}
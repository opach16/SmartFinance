package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionMapper {

    private final UserMapper userMapper;
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;

    public CurrencyTransaction mapToCurrencyTransactionEntity(CurrencyTransactionDto transactionDto) {
        return CurrencyTransaction.builder()
                .user(userMapper.mapToUserEntity(transactionDto.getUser()))
                .currency(currencyMapper.mapToCurrencyEntity(transactionDto.getCurrency()))
                .currencyTransactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .price(transactionDto.getPrice())
                .transactionDate(transactionDto.getTransactionDate())
                .build();
    }

    public CurrencyTransactionDto mapToCurrencyTransactionDto(CurrencyTransaction transaction) {
        Currency currency = currencyRepository.findBySymbol(transaction.getCurrency().getSymbol()).orElseThrow();
        return CurrencyTransactionDto.builder()
                .id(transaction.getId())
                .user(userMapper.mapToUserDto(transaction.getUser()))
                .currency(currencyMapper.mapToCurrencyDto(transaction.getCurrency()))
                .transactionType(transaction.getCurrencyTransactionType())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionValue(transaction.getAmount().multiply(transaction.getPrice()))
                .currentValue(transaction.getAmount().multiply(currency.getPrice()))
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .currencySymbol(transaction.getCurrency().getSymbol())
                .build();
    }

    public List<CurrencyTransaction> mapToCurrencyTransactionList(List<CurrencyTransactionDto> transactionDtoList) {
        return transactionDtoList.stream()
                .map(this::mapToCurrencyTransactionEntity)
                .toList();
    }

    public List<CurrencyTransactionDto> mapToCurrencyTransactionDtoList(List<CurrencyTransaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToCurrencyTransactionDto)
                .toList();
    }

    public CurrencyTransactionRequest mapToCurrencyTransactionRequest(CurrencyTransaction transaction) {
        return CurrencyTransactionRequest.builder()
                .userId(transaction.getUser().getId())
                .transactionType(transaction.getCurrencyTransactionType())
                .currency(transaction.getCurrency().getSymbol())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}

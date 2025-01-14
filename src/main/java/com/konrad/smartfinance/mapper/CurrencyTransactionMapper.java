package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionMapper {

    private final UserMapper userMapper;
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;

    public CurrencyTransactionDto mapToCurrencyTransactionDto(CurrencyTransaction transaction) {
        Currency currency = currencyRepository.findBySymbol(transaction.getCurrency().getSymbol()).orElseThrow();
        BigDecimal mainCurrencyPrice = transaction.getUser().getAccount().getMainCurrency().getPrice();
        return CurrencyTransactionDto.builder()
                .id(transaction.getId())
                .user(userMapper.mapToUserDto(transaction.getUser()))
                .currency(currencyMapper.mapToCurrencyDto(transaction.getCurrency()))
                .transactionType(transaction.getCurrencyTransactionType())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionValue(transaction.getAmount().multiply(transaction.getPrice()))
                .currentValue(calculateCurrentValue(transaction.getAmount(), currency.getPrice(), mainCurrencyPrice))
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .currencySymbol(transaction.getCurrency().getSymbol())
                .build();
    }

    public List<CurrencyTransactionDto> mapToCurrencyTransactionDtoList(List<CurrencyTransaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToCurrencyTransactionDto)
                .toList();
    }

    public CurrencyTransactionRequest mapToCurrencyTransactionRequest(CurrencyTransaction transaction) {
        return CurrencyTransactionRequest.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .transactionType(transaction.getCurrencyTransactionType())
                .currency(transaction.getCurrency().getSymbol())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

    private BigDecimal calculateCurrentValue(BigDecimal amount, BigDecimal price, BigDecimal mainUserCurrencyPrice) {
        return amount.multiply(price).divide(mainUserCurrencyPrice, 2, RoundingMode.CEILING);
    }
}

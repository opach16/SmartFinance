package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoTransactionMapper {

    private final UserMapper userMapper;
    private final CryptocurrencyMapper cryptocurrencyMapper;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    public CryptoTransactionDto mapToCryptoTransactionDto(CryptoTransaction transaction) {
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findById(transaction.getCryptocurrency().getId()).orElseThrow();
        BigDecimal mainPrice = transaction.getUser().getAccount().getMainCurrency().getPrice();
        return CryptoTransactionDto.builder()
                .id(transaction.getId())
                .user(userMapper.mapToUserDto(transaction.getUser()))
                .cryptocurrency(cryptocurrencyMapper.mapToCryptocurrencyDto(transaction.getCryptocurrency()))
                .transactionType(transaction.getCryptoTransactionType())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionValue(transaction.getAmount().multiply(transaction.getPrice()))
                .currentValue(calculateCurrentValue(transaction.getAmount(), cryptocurrency.getPrice(), mainPrice))
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .cryptocurrencySymbol(transaction.getCryptocurrency().getSymbol())
                .cryptocurrencyName(transaction.getCryptocurrency().getName())
                .build();
    }

    public List<CryptoTransactionDto> mapToCryptoTransactionDtoList(List<CryptoTransaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToCryptoTransactionDto)
                .toList();
    }

    public CryptoTransactionRequest mapToCryptoTransactionRequest(CryptoTransaction transaction) {
        return CryptoTransactionRequest.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .transactionType(transaction.getCryptoTransactionType())
                .cryptocurrency(transaction.getCryptocurrency().getSymbol())
                .name(transaction.getCryptocurrency().getName())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

    private BigDecimal calculateCurrentValue(BigDecimal amount, BigDecimal currentPrice, BigDecimal mainUserCurrencyPrice) {
        return amount.multiply(currentPrice).divide(mainUserCurrencyPrice, 2, RoundingMode.CEILING);
    }
}
package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoTransactionDto {

    private Long id;
    private UserDto user;
    private CryptocurrencyDto cryptocurrency;
    private String cryptocurrencySymbol;
    private String cryptocurrencyName;
    private CryptoTransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal value;
    private BigDecimal currentValue;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CryptoTransactionDto(UserDto user, CryptocurrencyDto cryptocurrency, CryptoTransactionType transactionType, BigDecimal amount, BigDecimal price, LocalDate transactionDate) {
        this.user = user;
        this.cryptocurrency = cryptocurrency;
        this.transactionType = transactionType;
        this.amount = amount;
        this.price = price;
        this.transactionDate = transactionDate;
    }
}

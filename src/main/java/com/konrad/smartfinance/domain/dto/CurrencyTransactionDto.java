package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.CurrencyTransactionType;
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
public class CurrencyTransactionDto {

    private Long id;
    private UserDto user;
    private CurrencyDto currency;
    private CurrencyTransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal value;
    private BigDecimal currentValue;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CurrencyTransactionDto(UserDto user, CurrencyDto currency, CurrencyTransactionType transactionType, BigDecimal amount, BigDecimal price, LocalDate transactionDate) {
        this.user = user;
        this.currency = currency;
        this.transactionType = transactionType;
        this.amount = amount;
        this.price = price;
        this.transactionDate = transactionDate;
    }
}

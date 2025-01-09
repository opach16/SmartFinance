package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.AccountTransactionType;
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
public class AccountTransactionDto {

    private Long id;
    private UserDto user;
    private AccountTransactionType transactionType;
    private String name;
    private CurrencyDto currency;
    private String currencySymbol;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal transactionValue;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountTransactionDto(UserDto user, AccountTransactionType transactionType, String name, CurrencyDto currency, BigDecimal amount, BigDecimal price, LocalDate transactionDate) {
        this.user = user;
        this.transactionType = transactionType;
        this.name = name;
        this.currency = currency;
        this.amount = amount;
        this.price = price;
        this.transactionDate = transactionDate;
    }
}

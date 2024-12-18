package com.konrad.smartfinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyTransactionDto {

    private Long id;
    private UserDto user;
    private CurrencyDto currency;
    private BigDecimal amount;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CurrencyTransactionDto(UserDto user, CurrencyDto currency, BigDecimal amount, BigDecimal price) {
        this.user = user;
        this.currency = currency;
        this.amount = amount;
        this.price = price;
    }
}

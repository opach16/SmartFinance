package com.konrad.smartfinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoTransactionDto {

    private Long id;
    private UserDto user;
    private CryptocurrencyDto cryptocurrency;
    private BigDecimal amount;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CryptoTransactionDto(UserDto user, CryptocurrencyDto cryptocurrency, BigDecimal amount, BigDecimal price) {
        this.user = user;
        this.cryptocurrency = cryptocurrency;
        this.amount = amount;
        this.price = price;
    }
}

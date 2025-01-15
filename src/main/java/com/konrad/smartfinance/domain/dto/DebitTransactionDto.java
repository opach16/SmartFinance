package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.DebitTransactionType;
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
public class DebitTransactionDto {

    private Long id;
    private UserDto user;
    private DebitTransactionType transactionType;
    private String name;
    private String currencySymbol;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal transactionValue;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DebitTransactionDto(UserDto user, DebitTransactionType transactionType, String name, BigDecimal amount, BigDecimal price, LocalDate transactionDate) {
        this.user = user;
        this.transactionType = transactionType;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.transactionDate = transactionDate;
    }
}

package com.konrad.smartfinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDto {

    private Long id;

    private UserDto user;

    private String name;

    private String description;

    private CurrencyDto currency;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public IncomeDto(UserDto user, String name, String description, CurrencyDto currency, BigDecimal amount) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
    }

}

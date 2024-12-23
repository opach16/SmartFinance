package com.konrad.smartfinance.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private Long id;
    private String symbol;
    private String name;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CurrencyDto(String symbol, String name, BigDecimal price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

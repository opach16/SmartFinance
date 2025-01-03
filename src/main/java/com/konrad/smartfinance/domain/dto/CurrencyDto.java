package com.konrad.smartfinance.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private Long id;
    private String symbol;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CurrencyDto(String symbol,  BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
    }
}

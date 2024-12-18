package com.konrad.smartfinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private Long id;
    private String symbol;
    private String name;
    private String price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CurrencyDto(String symbol, String name, String price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

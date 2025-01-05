package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.CurrencyTransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CurrencyTransactionRequest {

    private Long userId;
    private CurrencyTransactionType transactionType;
    private String currency;
    private BigDecimal amount;
    private BigDecimal price;
    private LocalDate transactionDate;
}

package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CryptoTransactionRequest {

    private Long userId;
    private CryptoTransactionType transactionType;
    private String cryptocurrency;
    private BigDecimal amount;
    private BigDecimal price;
    private LocalDate transactionDate;
}

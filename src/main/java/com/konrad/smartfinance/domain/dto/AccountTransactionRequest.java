package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.AccountTransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AccountTransactionRequest {

    private Long userId;
    private AccountTransactionType transactionType;
    private String name;
    private String currency;
    private BigDecimal amount;
    private LocalDate transactionDate;
}

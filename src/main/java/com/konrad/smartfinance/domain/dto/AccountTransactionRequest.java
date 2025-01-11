package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.AccountTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountTransactionRequest {

    private Long userId;
    private AccountTransactionType transactionType;
    private String name;
    private String currency;
    private BigDecimal amount;
    private BigDecimal price;
    private LocalDate transactionDate;
}

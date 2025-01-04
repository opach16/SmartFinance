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
public class AccountDto {

    private Long id;
    private UserDto user;
    private CurrencyDto mainCurrency;
    private BigDecimal mainBalance;
    private BigDecimal totalBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountDto(UserDto user, CurrencyDto mainCurrency, BigDecimal mainBalance) {
        this.user = user;
        this.mainCurrency = mainCurrency;
        this.mainBalance = mainBalance;
    }
}

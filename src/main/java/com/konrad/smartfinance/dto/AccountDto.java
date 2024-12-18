package com.konrad.smartfinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private UserDto user;
    private String mainCurrency;
    private BigDecimal mainBalance;
    private BigDecimal totalBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountDto(UserDto user, String mainCurrency, BigDecimal mainBalance, BigDecimal totalBalance) {
        this.user = user;
        this.mainCurrency = mainCurrency;
        this.mainBalance = mainBalance;
    }
}

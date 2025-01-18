package com.konrad.smartfinance.domain.dto;

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
    private CurrencyDto mainCurrency;
    private BigDecimal mainBalance;
    private BigDecimal assetsBalance;
    private BigDecimal totalBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountDto(UserDto user, CurrencyDto mainCurrency, BigDecimal mainBalance) {
        this.user = user;
        this.mainCurrency = mainCurrency;
        this.mainBalance = mainBalance;
    }

    public static class Builder {
        private Long id;
        private UserDto user;
        private CurrencyDto mainCurrency;
        private BigDecimal mainBalance;
        private BigDecimal assetsBalance;
        private BigDecimal totalBalance;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder user(UserDto user) {
            this.user = user;
            return this;
        }

        public Builder mainCurrency(CurrencyDto mainCurrency) {
            this.mainCurrency = mainCurrency;
            return this;
        }

        public Builder mainBalance(BigDecimal mainBalance) {
            this.mainBalance = mainBalance;
            return this;
        }

        public Builder assetsBalance(BigDecimal assetsBalance) {
            this.assetsBalance = assetsBalance;
            return this;
        }

        public Builder totalBalance(BigDecimal totalBalance) {
            this.totalBalance = totalBalance;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public AccountDto build() {
            return new AccountDto(id, user, mainCurrency, mainBalance, assetsBalance, totalBalance, createdAt, updatedAt);
        }
    }
}

package com.konrad.smartfinance.dto;

import com.konrad.smartfinance.model.Currency;
import com.konrad.smartfinance.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDto {

    private Long id;

    private User user;

    private String name;

    private String description;

    private Currency currency;

    private double amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public IncomeDto(User user, String name, String description, Currency currency, double amount) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
    }

}

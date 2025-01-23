package com.konrad.smartfinance.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateUserRequestDto {

    private UserDto user;
    private String mainCurrency;
    private BigDecimal mainBalance;
}

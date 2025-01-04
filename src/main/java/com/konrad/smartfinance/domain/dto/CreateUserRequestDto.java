package com.konrad.smartfinance.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateUserRequestDto {

    private UserDto user;
    private String mainCurrency;
    private BigDecimal mainBalance;
}

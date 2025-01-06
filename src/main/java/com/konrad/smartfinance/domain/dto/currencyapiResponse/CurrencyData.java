package com.konrad.smartfinance.domain.dto.currencyapiResponse;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyData {

    private String code;
    private BigDecimal value;
}

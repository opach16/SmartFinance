package com.konrad.smartfinance.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.konrad.smartfinance.domain.CurrencyTransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyTransactionRequest {

    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("transactionType")
    private CurrencyTransactionType transactionType;
    @JsonProperty("symbol")
    private String currency;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("transactionDate")
    private LocalDate transactionDate;
}

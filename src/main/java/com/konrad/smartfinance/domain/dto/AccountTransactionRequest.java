package com.konrad.smartfinance.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountTransactionRequest {

    @JsonProperty("id")
    private Long transactionId;
    @JsonProperty("transactionType")
    private AccountTransactionType transactionType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("transactionDate")
    private LocalDate transactionDate;
}

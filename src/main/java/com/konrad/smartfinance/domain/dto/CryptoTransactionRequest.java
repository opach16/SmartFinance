package com.konrad.smartfinance.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.konrad.smartfinance.domain.CryptoTransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CryptoTransactionRequest {

    @JsonProperty("transactionId")
    private Long id;
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("transactionType")
    private CryptoTransactionType transactionType;
    @JsonProperty("symbol")
    private String cryptocurrency;
    @JsonProperty("name")
    private String name;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("transactionDate")
    private LocalDate transactionDate;
}

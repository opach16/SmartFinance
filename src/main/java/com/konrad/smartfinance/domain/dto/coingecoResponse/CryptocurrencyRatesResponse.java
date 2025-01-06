package com.konrad.smartfinance.domain.dto.coingecoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CryptocurrencyRatesResponse {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("name")
    private String name;
    @JsonProperty("current_price")
    private String currentPrice;
}

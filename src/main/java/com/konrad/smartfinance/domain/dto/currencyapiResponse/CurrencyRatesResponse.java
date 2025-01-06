package com.konrad.smartfinance.domain.dto.currencyapiResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRatesResponse {

    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("data")
    private Map<String, CurrencyData> data;
}

package com.konrad.smartfinance.client;

import com.konrad.smartfinance.domain.dto.currencyapiResponse.CurrencyRatesResponse;
import com.konrad.smartfinance.domain.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrencyapiClient {

    public static final String CURRENCY_SYMBOLS = "PLN,USD,EUR,GBP,CHF";

    private final RestTemplate restTemplate;

    @Value("${currencyapi.api.endpoint}")
    private String endpoint;
    @Value("${currencyapi.api.key}")
    private String apiKey;

    public CurrencyRatesResponse fetchAllCurrencies(String baseCurrency) {
        URI url = UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("apikey", apiKey)
                .queryParam("currencies", CURRENCY_SYMBOLS)
                .queryParam("base_currency", baseCurrency)
                .build()
                .encode()
                .toUri();
        CurrencyRatesResponse response = restTemplate.getForObject(url, CurrencyRatesResponse.class);
        return Optional.ofNullable(response).orElse(new CurrencyRatesResponse());
    }

    public List<Currency> fetchCurrencies() {
        CurrencyRatesResponse response = fetchAllCurrencies("PLN");
        return response.getData().entrySet().stream()
                .map(entry -> new Currency(entry.getValue().getCode(), BigDecimal.ONE.divide(entry.getValue().getValue(), 2, RoundingMode.CEILING)))
                .toList();
    }
}

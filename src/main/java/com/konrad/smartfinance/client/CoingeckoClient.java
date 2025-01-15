package com.konrad.smartfinance.client;

import com.konrad.smartfinance.domain.dto.coingecoResponse.CryptocurrencyRatesResponse;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CoingeckoClient {

    public static final String CRYPTO_IDS = "bitcoin,ethereum,ripple,tether,binancecoin,dogecoin";
    public static final String BASE_CURRENCY = "pln";

    private final RestTemplate restTemplate;

    @Value("${coingecko.api.endpoint}")
    private String endpoint;
    @Value("${coingecko.api.key}")
    private String apiKey;

    public List<Cryptocurrency> fetchCryptocurrencies() {
        URI url = UriComponentsBuilder.fromUriString(endpoint + "/coins/markets")
                .queryParam("x-cg-pro-api-key", apiKey)
                .queryParam("ids", CRYPTO_IDS)
                .queryParam("vs_currency", BASE_CURRENCY)
                .build()
                .encode()
                .toUri();
        CryptocurrencyRatesResponse[] response = restTemplate.getForObject(url, CryptocurrencyRatesResponse[].class);
        return response != null ? Arrays.stream(response).map(a -> new Cryptocurrency(a.getSymbol(), a.getName(), new BigDecimal(a.getCurrentPrice()))).toList() : Collections.emptyList();
    }
}

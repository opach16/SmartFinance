package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.client.CurrencyapiClient;
import com.konrad.smartfinance.domain.dto.CurrencyDto;
import com.konrad.smartfinance.domain.dto.currencyapiResponse.CurrencyRatesResponse;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.mapper.CurrencyMapper;
import com.konrad.smartfinance.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyapiClient currencyapiClient;
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        return ResponseEntity.ok().body(currencyMapper.mapToCurrencyDtoList(currencyService.getAllCurrencies()));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CurrencyDto> getCurrencyBySymbol(@PathVariable String symbol) throws CurrencyExeption {
        return ResponseEntity.ok().body(currencyMapper.mapToCurrencyDto(currencyService.getCurrencyBySymbol(symbol)));
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getCurrencyPrice(@PathVariable String symbol) throws CurrencyExeption {
        return ResponseEntity.ok().body(currencyService.getCurrencyPrice(symbol));
    }

    @GetMapping("/abc")
    public ResponseEntity<Void> getAllCurrenciesByAbc() {
        CurrencyRatesResponse currencyRatesResponse = currencyapiClient.fetchAllCurrencies("PLN");
        System.out.println(currencyRatesResponse.getMeta().getLastUpdatedAt());
        currencyRatesResponse.getData().entrySet().stream().forEach(entry -> System.out.println(entry.getValue().getCode() + " : " + entry.getValue().getValue()));
        return ResponseEntity.ok().build();
    }
}

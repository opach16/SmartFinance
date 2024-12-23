package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CryptocurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoController {

    @GetMapping
    public ResponseEntity<List<CryptocurrencyDto>> getAllCryptocurrencies() {
        return ResponseEntity.ok().body(new ArrayList<CryptocurrencyDto>());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CryptocurrencyDto> getCryptocurrenciesByUserId(@PathVariable String symbol) {
        return ResponseEntity.ok().body(new CryptocurrencyDto("test", "test", new BigDecimal("15")));
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getPriceBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok().body(new BigDecimal("50.15"));
    }
}

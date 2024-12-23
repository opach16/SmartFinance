package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        return ResponseEntity.ok().body(new ArrayList<CurrencyDto>());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CurrencyDto> getCurrencyByCode(@PathVariable String symbol) {
        return ResponseEntity.ok().body(new CurrencyDto(1L, "test", "test",
                new BigDecimal(1), LocalDateTime.now(), null));
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getCurrencyBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok().body(new BigDecimal("50.15"));
    }
}

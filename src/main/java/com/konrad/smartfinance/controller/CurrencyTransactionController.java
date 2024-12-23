package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currency-transactions")
public class CurrencyTransactionController {

    @GetMapping
    public ResponseEntity<List<CurrencyTransactionDto>> getAllCurrencyTransactions() {
        return ResponseEntity.ok().body(new ArrayList<CurrencyTransactionDto>());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CurrencyTransactionDto>> getCurrencyTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok().body(new ArrayList<CurrencyTransactionDto>());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyTransactionDto> getCurrencyTransaction(@PathVariable Long id) {
        return ResponseEntity.ok().body(new CurrencyTransactionDto());
    }

    @PostMapping
    public ResponseEntity<CurrencyTransactionDto> addCurrencyTransaction(@RequestBody CurrencyTransactionDto currencyTransactionDto) {
        return ResponseEntity.ok().body(new CurrencyTransactionDto());
    }
}

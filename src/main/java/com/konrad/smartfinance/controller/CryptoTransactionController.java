package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/crypto-transactions")
public class CryptoTransactionController {

    @GetMapping
    public ResponseEntity<List<CryptoTransactionDto>> getAllCryptoTransactions() {
        return ResponseEntity.ok().body(new ArrayList<CryptoTransactionDto>());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CryptoTransactionDto>> getCryptoTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok().body(new ArrayList<CryptoTransactionDto>());
    }

    @PostMapping
    public ResponseEntity<CryptoTransactionDto> addCryptoTransaction(@RequestBody CryptoTransactionDto cryptoTransactionDto) {
        return ResponseEntity.ok().body(cryptoTransactionDto);
    }
}
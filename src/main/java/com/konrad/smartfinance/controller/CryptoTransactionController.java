package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.exception.CryptoTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.CryptoTransactionMapper;
import com.konrad.smartfinance.service.CryptoTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/crypto-transactions")
public class CryptoTransactionController {

    private final CryptoTransactionService cryptoTransactionService;
    private final CryptoTransactionMapper cryptoTransactionMapper;

    @GetMapping
    public ResponseEntity<List<CryptoTransactionDto>> getAllCryptoTransactions() {
        return ResponseEntity.ok().body(cryptoTransactionMapper.mapToCryptoTransactionDtoList(cryptoTransactionService.getAllTransactions()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CryptoTransactionDto>> getCryptoTransactionsByUserId(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok().body(cryptoTransactionMapper
                .mapToCryptoTransactionDtoList(cryptoTransactionService.getTransactionsByUserId(userId)));
    }

    @PostMapping
    public ResponseEntity<CryptoTransactionDto> addCryptoTransaction(@RequestBody CryptoTransactionDto transactionDto) {
        CryptoTransaction transaction = cryptoTransactionMapper.mapToCryptoTransaction(transactionDto);
        return ResponseEntity.ok().body(cryptoTransactionMapper
                .mapToCryptoTransactionDto(cryptoTransactionService.addTransaction(transaction)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CryptoTransactionDto> updateCryptoTransaction(@PathVariable Long id, @RequestBody CryptoTransactionDto transactionDto) throws CryptoTransactionException {
        CryptoTransaction transaction = cryptoTransactionMapper.mapToCryptoTransaction(transactionDto);
        return ResponseEntity.ok().body(cryptoTransactionMapper
                .mapToCryptoTransactionDto(cryptoTransactionService.updateTransaction(id, transaction)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCryptoTransaction(@PathVariable Long id) throws CryptoTransactionException {
        cryptoTransactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}
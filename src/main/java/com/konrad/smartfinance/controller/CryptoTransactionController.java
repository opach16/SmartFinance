package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.CryptoTransactionException;
import com.konrad.smartfinance.exception.CryptocurrencyException;
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
    public ResponseEntity<List<CryptoTransactionDto>> getAllTransactions() {
        return ResponseEntity.ok().body(cryptoTransactionMapper.mapToCryptoTransactionDtoList(cryptoTransactionService.getAllTransactions()));
    }

    @GetMapping("{id}")
    public ResponseEntity<CryptoTransactionDto> getTransactionById(@PathVariable Long id) throws CryptoTransactionException {
        return ResponseEntity.ok().body(cryptoTransactionMapper
                .mapToCryptoTransactionDto(cryptoTransactionService.getTransactionById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CryptoTransactionDto>> getTransactionsByUserId(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok().body(cryptoTransactionMapper
                .mapToCryptoTransactionDtoList(cryptoTransactionService.getTransactionsByUserId(userId)));
    }

    @PostMapping
    public ResponseEntity<CryptoTransactionDto> addTransaction(@RequestBody CryptoTransactionRequest request) throws CryptocurrencyException, UserException, AccountException {
        CryptoTransaction transaction = cryptoTransactionService.addTransaction(request);
        return ResponseEntity.ok().body(cryptoTransactionMapper.mapToCryptoTransactionDto(transaction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CryptoTransactionDto> updateTransaction(@PathVariable Long id, @RequestBody CryptoTransactionRequest request) throws CryptoTransactionException, CryptocurrencyException, AccountException {
        CryptoTransaction transaction = cryptoTransactionService.updateTransaction(id, request);
        return ResponseEntity.ok().body(cryptoTransactionMapper.mapToCryptoTransactionDto(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCryptoTransaction(@PathVariable Long id) throws CryptoTransactionException, AccountException {
        cryptoTransactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}
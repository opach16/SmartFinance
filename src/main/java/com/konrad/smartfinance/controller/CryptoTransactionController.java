package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.exception.*;
import com.konrad.smartfinance.mapper.CryptoTransactionMapper;
import com.konrad.smartfinance.service.CryptoTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoTransactionRequest> addTransactionWithParams(
            @RequestBody CryptoTransactionRequest request,
            @PathVariable Long userId) throws AccountException, CryptocurrencyException, UserException {
        CryptoTransaction response = cryptoTransactionService.addTransaction(request, userId);
        return ResponseEntity.ok().body(cryptoTransactionMapper.mapToCryptoTransactionRequest(response));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CryptoTransactionRequest> updateTransactionWithParams(
          @RequestBody CryptoTransactionRequest request
    ) throws AccountException, CryptoTransactionException, CryptocurrencyException {
        CryptoTransaction transaction = cryptoTransactionService.updateTransaction(request);
        return ResponseEntity.ok().body(cryptoTransactionMapper.mapToCryptoTransactionRequest(transaction));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCryptoTransaction(@RequestParam Long transactionId) throws CryptoTransactionException, AccountException {
        cryptoTransactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }
}
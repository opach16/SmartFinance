package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.exception.*;
import com.konrad.smartfinance.mapper.CurrencyTransactionMapper;
import com.konrad.smartfinance.service.CurrencyTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currency-transactions")
@CrossOrigin("*")
public class CurrencyTransactionController {

    private final CurrencyTransactionService currencyTransactionService;
    private final CurrencyTransactionMapper currencyTransactionMapper;

    @GetMapping
    public ResponseEntity<List<CurrencyTransactionDto>> getAllCurrencyTransactions() {
        return ResponseEntity.ok().body(currencyTransactionMapper
                .mapToCurrencyTransactionDtoList(currencyTransactionService.getAllTransactions()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyTransactionDto> getTransactionById(@PathVariable Long id) throws CurrencyTransactionException {
        return ResponseEntity.ok().body(currencyTransactionMapper
                .mapToCurrencyTransactionDto(currencyTransactionService.getTransactionById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CurrencyTransactionDto>> getTransactionsByUserId(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok().body(currencyTransactionMapper
                .mapToCurrencyTransactionDtoList(currencyTransactionService.getTransactionByUserId(userId)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyTransactionRequest> addTransaction(
            @RequestBody CurrencyTransactionRequest request) throws CurrencyExeption, AccountException, UserException, AssetException {
        CurrencyTransaction transaction = currencyTransactionService.addTransaction(request);
        return ResponseEntity.ok().body(currencyTransactionMapper.mapToCurrencyTransactionRequest(transaction));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyTransactionRequest> updateTransaction(
            @RequestBody CurrencyTransactionRequest request) throws CurrencyExeption, AccountException, CurrencyTransactionException, AssetException, UserException {
        CurrencyTransaction transaction = currencyTransactionService.updateTransaction(request);
        return ResponseEntity.ok().body(currencyTransactionMapper.mapToCurrencyTransactionRequest(transaction));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTransaction(@RequestParam Long transactionId) throws CurrencyTransactionException, AccountException, AssetException {
        currencyTransactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }
}

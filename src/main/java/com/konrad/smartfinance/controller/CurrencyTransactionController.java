package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.CurrencyTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.CurrencyTransactionMapper;
import com.konrad.smartfinance.service.CurrencyTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currency-transactions")
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

    @PostMapping
    public ResponseEntity<CurrencyTransactionDto> addTransaction(@RequestBody CurrencyTransactionRequest request) throws CurrencyExeption, UserException {
        CurrencyTransaction transaction = currencyTransactionService.addTransaction(request);
        return ResponseEntity.ok().body(currencyTransactionMapper.mapToCurrencyTransactionDto(transaction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyTransactionDto> updateTransaction(@PathVariable Long id, @RequestBody CurrencyTransactionRequest request) throws CurrencyTransactionException, CurrencyExeption {
        CurrencyTransaction transaction = currencyTransactionService.updateTransaction(id, request);
        return ResponseEntity.ok().body(currencyTransactionMapper.mapToCurrencyTransactionDto(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) throws CurrencyTransactionException {
        currencyTransactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}

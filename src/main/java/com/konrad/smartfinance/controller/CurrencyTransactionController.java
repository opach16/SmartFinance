package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.exception.CurrencyTransactionException;
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

    @GetMapping("/{userId}")
    public ResponseEntity<List<CurrencyTransactionDto>> getTransactionsByUserId(@PathVariable Long userId) throws CurrencyTransactionException {
        return ResponseEntity.ok().body(currencyTransactionMapper
                .mapToCurrencyTransactionDtoList(currencyTransactionService.getTransactionByUserId(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyTransactionDto> getTransactionById(@PathVariable Long id) throws CurrencyTransactionException {
        return ResponseEntity.ok().body(currencyTransactionMapper
                .mapToCurrencyTransactionDto(currencyTransactionService.getTransactionById(id)));
    }

    @PostMapping
    public ResponseEntity<CurrencyTransactionDto> addTransaction(@RequestBody CurrencyTransactionDto transactionDto) {
        CurrencyTransaction transaction = currencyTransactionMapper.mapToCurrencyTransactionEntity(transactionDto);
        return ResponseEntity.ok().body(currencyTransactionMapper
                .mapToCurrencyTransactionDto(currencyTransactionService.addTransaction(transaction)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyTransactionDto> updateTransaction(@PathVariable Long id, @RequestBody CurrencyTransactionDto transactionDto) throws CurrencyTransactionException {
        CurrencyTransaction transaction = currencyTransactionService.updateTransaction(id, currencyTransactionMapper.mapToCurrencyTransactionEntity(transactionDto));
        return ResponseEntity.ok().body(currencyTransactionMapper.mapToCurrencyTransactionDto(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) throws CurrencyTransactionException {
        currencyTransactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}

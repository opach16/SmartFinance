package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.CurrencyTransactionType;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.CurrencyTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.CurrencyTransactionMapper;
import com.konrad.smartfinance.service.CurrencyTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @PostMapping
    public ResponseEntity<CurrencyTransactionRequest> addTransactionWithParameters(
            @RequestParam Long userId,
            @RequestParam CurrencyTransactionType transactionType,
            @RequestParam String currency,
            @RequestParam BigDecimal amount,
            @RequestParam BigDecimal price,
            @RequestParam LocalDate transactionDate
    ) throws CurrencyExeption, AccountException, UserException {
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .userId(userId)
                .transactionType(transactionType)
                .currency(currency)
                .amount(amount)
                .price(price)
                .transactionDate(transactionDate)
                .build();
        CurrencyTransaction transaction = currencyTransactionService.addTransaction(request);
        return ResponseEntity.ok().body(currencyTransactionMapper.mapToCurrencyTransactionRequest(transaction));
    }

    @PutMapping
    public ResponseEntity<CurrencyTransactionRequest> updateTransactionWithParams(
            @RequestParam Long transactionId,
            @RequestParam Long userId,
            @RequestParam CurrencyTransactionType transactionType,
            @RequestParam String currency,
            @RequestParam BigDecimal amount,
            @RequestParam BigDecimal price,
            @RequestParam LocalDate transactionDate
    ) throws CurrencyExeption, AccountException, CurrencyTransactionException {
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .userId(userId)
                .transactionType(transactionType)
                .currency(currency)
                .amount(amount)
                .price(price)
                .transactionDate(transactionDate)
                .build();
        CurrencyTransaction transaction = currencyTransactionService.updateTransaction(transactionId, request);
        return ResponseEntity.ok().body(currencyTransactionMapper.mapToCurrencyTransactionRequest(transaction));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTransactionWithParams(@RequestParam Long transactionId) throws CurrencyTransactionException, AccountException {
        currencyTransactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }
}

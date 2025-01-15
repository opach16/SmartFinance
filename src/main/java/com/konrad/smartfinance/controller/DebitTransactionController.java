package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.DebitTransactionDto;
import com.konrad.smartfinance.domain.dto.DebitTransactionRequest;
import com.konrad.smartfinance.domain.model.DebitTransaction;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.DebitTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.DebitTransactionMapper;
import com.konrad.smartfinance.service.DebitTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/debit-transactions")
public class DebitTransactionController {

    private final DebitTransactionMapper debitTransactionMapper;
    private final DebitTransactionService debitTransactionService;


    @GetMapping("/{id}")
    public ResponseEntity<DebitTransactionDto> getTransactionById(@PathVariable Long id) throws DebitTransactionException {
        return ResponseEntity.ok(debitTransactionMapper.mapToAccountTransactionDto(debitTransactionService.getTransactionById(id)));
    }

    @GetMapping("/{accountId}/all")
    public ResponseEntity<List<DebitTransactionDto>> getAllTransactions(@PathVariable Long accountId) throws AccountException {
        return ResponseEntity.ok(debitTransactionMapper.mapToAccountTransactionDtoList(debitTransactionService.getAllTransactions(accountId)));
    }

    @GetMapping("/{accountId}/expenses")
    public ResponseEntity<List<DebitTransactionDto>> getExpensesList(@PathVariable Long accountId) throws AccountException {
        return ResponseEntity.ok(debitTransactionMapper.mapToAccountTransactionDtoList(debitTransactionService.getAllExpenses(accountId)));
    }

    @GetMapping("/{accountId}/incomes")
    public ResponseEntity<List<DebitTransactionDto>> getIncomesList(@PathVariable Long accountId) throws AccountException {
        return ResponseEntity.ok(debitTransactionMapper.mapToAccountTransactionDtoList(debitTransactionService.getAllIncomes(accountId)));
    }

    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DebitTransactionRequest> addTransaction(
            @RequestBody DebitTransactionRequest request, @PathVariable Long userId) throws AccountException, UserException {
        DebitTransaction transaction = debitTransactionService.addTransaction(request, userId);
        return ResponseEntity.ok().body(debitTransactionMapper.mapToAccountTransactionRequest(transaction));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DebitTransactionRequest> addTransaction(
            @RequestBody DebitTransactionRequest request) throws AccountException, DebitTransactionException {
        DebitTransaction transaction = debitTransactionService.updateTransaction(request);
        return ResponseEntity.ok().body(debitTransactionMapper.mapToAccountTransactionRequest(transaction));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTransaction(@RequestParam Long transactionId) throws DebitTransactionException, AccountException {
        debitTransactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }
}

package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.domain.dto.AccountTransactionDto;
import com.konrad.smartfinance.domain.dto.AccountTransactionRequest;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.AccountTransactionException;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.AccountMapper;
import com.konrad.smartfinance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id) throws AccountException {
        return ResponseEntity.ok(accountMapper.mapToAccountDto(accountService.getAccountById(id)));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<AccountTransactionDto>> getAllTransactions(@PathVariable Long accountId) throws AccountException {
        return ResponseEntity.ok(accountMapper.mapToAccountTransactionDtoList(accountService.getAllTransactions(accountId)));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<AccountTransactionDto> getTransactionById(@PathVariable Long id) throws AccountTransactionException {
        return ResponseEntity.ok(accountMapper.mapToAccountTransactionDto(accountService.getTransactionById(id)));
    }

    @GetMapping("/{accountId}/transactions/expenses")
    public ResponseEntity<List<AccountTransactionDto>> getExpensesList(@PathVariable Long accountId) throws AccountException {
        return ResponseEntity.ok(accountMapper.mapToAccountTransactionDtoList(accountService.getAllExpenses(accountId)));
    }

    @GetMapping("/{accountId}/transactions/incomes")
    public ResponseEntity<List<AccountTransactionDto>> getIncomesList(@PathVariable Long accountId) throws AccountException {
        return ResponseEntity.ok(accountMapper.mapToAccountTransactionDtoList(accountService.getAllIncomes(accountId)));
    }

    @PostMapping("/transactions")
    public ResponseEntity<AccountTransactionDto> addTransaction(@RequestBody AccountTransactionRequest request) throws CurrencyExeption, UserException, AccountException {
        return ResponseEntity.ok(accountMapper.mapToAccountTransactionDto(accountService.addTransaction(request)));
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<AccountTransactionDto> updateTransaction(@PathVariable Long id, @RequestBody AccountTransactionRequest request) throws AccountTransactionException, CurrencyExeption, UserException, AccountException {
        return ResponseEntity.ok(accountMapper.mapToAccountTransactionDto(accountService.updateTransaction(id, request)));
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) throws AccountTransactionException, AccountException {
        accountService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }

    @GetMapping("/total-balance/{id}")
    public ResponseEntity<BigDecimal> getTotalBalance(@PathVariable Long id) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }
}

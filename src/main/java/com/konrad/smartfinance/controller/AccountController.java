package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.AccountTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @GetMapping(value = "/balance/{id}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }

    @GetMapping(value = "/total-balance/{id}")
    public ResponseEntity<BigDecimal> getTotalBalance(@PathVariable Long id) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }

    @GetMapping(value = "/expenses/{id}")
    public ResponseEntity<List> getExpansesList(@PathVariable Long id) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping(value = "/expanses")
    public ResponseEntity<AccountTransactionDto> addExpanse(@RequestBody AccountTransactionDto accountTransactionDto) {
        return ResponseEntity.ok(accountTransactionDto);
    }

    @PutMapping(value = "/expanses")
    public ResponseEntity<AccountTransactionDto> updateExpense(@RequestBody AccountTransactionDto accountTransactionDto) {
        return ResponseEntity.ok(accountTransactionDto);
    }

    @GetMapping(value = "/incomes/{id}")
    public ResponseEntity<List> getIncomesList(@PathVariable Long id) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping(value = "/incomes")
    public ResponseEntity<AccountTransactionDto> addIncome(@RequestBody AccountTransactionDto accountTransactionDto) {
        return ResponseEntity.ok(accountTransactionDto);
    }

    @PutMapping(value = "/incomes")
    public ResponseEntity<AccountTransactionDto> updateIncome(@RequestBody AccountTransactionDto accountTransactionDto) {
        return ResponseEntity.ok(accountTransactionDto);
    }
}

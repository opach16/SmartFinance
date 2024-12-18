package com.konrad.smartfinance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value ="/expenses/{id}")
    public ResponseEntity<List> getExpansesList(@PathVariable Long id) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping(value = "/incomes/{id}")
    public ResponseEntity<List> getIncomesList(@PathVariable Long id) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}

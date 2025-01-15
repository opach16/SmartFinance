package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.mapper.AccountMapper;
import com.konrad.smartfinance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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

    @GetMapping("/balance/{id}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }

    @GetMapping("/total-balance/{id}")
    public ResponseEntity<BigDecimal> getTotalBalance(@PathVariable Long id) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }
}

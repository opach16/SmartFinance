package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.exception.UserException;
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

    @GetMapping("/{userId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok(accountMapper.mapToAccountDto(accountService.getAccountByUserId(userId)));
    }

    @GetMapping("/main-balance/{userId}")
    public ResponseEntity<BigDecimal> getMainBalance(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok(accountService.getMainBalanceByUserId(userId));
    }

    @GetMapping("/assets-balance/{userId}")
    public ResponseEntity<BigDecimal> getAssetsBalance(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok(accountService.getAssetsBalanceByUserId(userId));
    }

    @GetMapping("/total-balance/{userId}")
    public ResponseEntity<BigDecimal> getTotalBalance(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok(accountService.getTotalBalanceByUserId(userId));
    }
}

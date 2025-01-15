package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) throws AccountException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
    }
}

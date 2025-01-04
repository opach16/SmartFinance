package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.AccountTransactionType;
import com.konrad.smartfinance.domain.dto.AccountTransactionRequest;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.AccountTransaction;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.AccountTransactionException;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.AccountRepository;
import com.konrad.smartfinance.repository.AccountTransactionRepository;
import com.konrad.smartfinance.repository.CurrencyRepository;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) throws AccountException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
    }

    public List<AccountTransaction> getAllTransactions(Long accountId) throws AccountException {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        return accountTransactionRepository.findAll();
    }

    public List<AccountTransaction> getAllExpenses(Long accountId) throws AccountException {
        accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        return accountTransactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == AccountTransactionType.EXPENSE)
                .toList();
    }

    public AccountTransaction getTransactionById(Long id) throws AccountTransactionException {
        return accountTransactionRepository.findById(id)
                .orElseThrow(() -> new AccountTransactionException(AccountTransactionException.NOT_FOUND));
    }

    public AccountTransaction addTransaction(AccountTransactionRequest request) throws UserException, CurrencyExeption {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        Currency currency = currencyRepository.findBySymbol(request.getCurrency())
                .orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        AccountTransaction transaction = new AccountTransaction();
        transaction.setUser(user);
        transaction.setTransactionType(request.getTransactionType());
        transaction.setName(request.getName());
        transaction.setCurrency(currency);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(request.getTransactionDate());
        return accountTransactionRepository.save(transaction);
    }

    public AccountTransaction updateTransaction(Long id, AccountTransactionRequest request) throws AccountTransactionException, UserException, CurrencyExeption {
        AccountTransaction fetchedTransaction = accountTransactionRepository.findById(id)
                .orElseThrow(() -> new AccountTransactionException(AccountTransactionException.NOT_FOUND));
        Currency currency = currencyRepository.findBySymbol(request.getCurrency())
                .orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        fetchedTransaction.setTransactionType(request.getTransactionType());
        fetchedTransaction.setName(request.getName());
        fetchedTransaction.setCurrency(currency);
        fetchedTransaction.setAmount(request.getAmount());
        fetchedTransaction.setTransactionDate(request.getTransactionDate());
        return accountTransactionRepository.save(fetchedTransaction);
    }

    public void deleteTransaction(Long id) throws AccountTransactionException {
        accountTransactionRepository.findById(id).orElseThrow(() -> new AccountTransactionException(AccountTransactionException.NOT_FOUND));
        accountTransactionRepository.deleteById(id);
    }

    public List<AccountTransaction> getAllIncomes(Long accountId) throws AccountException {
        accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        return accountTransactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == AccountTransactionType.INCOME)
                .toList();
    }
}

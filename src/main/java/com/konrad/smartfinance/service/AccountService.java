package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.AccountTransactionType;
import com.konrad.smartfinance.domain.dto.AccountTransactionRequest;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.AccountTransaction;
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

import java.math.BigDecimal;
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
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));

        return accountTransactionRepository.findByUser(account.getUser());
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

    public AccountTransaction addTransaction(AccountTransactionRequest request) throws UserException, CurrencyExeption, AccountException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        AccountTransaction transaction = AccountTransaction.builder()
                .user(user)
                .transactionType(request.getTransactionType())
                .name(request.getName())
                .amount(request.getAmount())
                .price(request.getPrice())
                .transactionDate(request.getTransactionDate())
                .build();
        AccountTransaction savedTransaction = accountTransactionRepository.save(transaction);
        updateAccountBalance(savedTransaction, true);
        return savedTransaction;
    }

    public AccountTransaction updateTransaction(Long id, AccountTransactionRequest request) throws AccountTransactionException, CurrencyExeption, UserException, AccountException {
        AccountTransaction transaction = accountTransactionRepository.findById(id)
                .orElseThrow(() -> new AccountTransactionException(AccountTransactionException.NOT_FOUND));
        updateAccountBalance(transaction, false);
        transaction.setTransactionType(request.getTransactionType());
        transaction.setName(request.getName());
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        AccountTransaction updatedTransaction = accountTransactionRepository.save(transaction);
        updateAccountBalance(updatedTransaction, true);
        return updatedTransaction;
    }

    public void deleteTransaction(Long id) throws AccountTransactionException, AccountException {
        AccountTransaction transaction = accountTransactionRepository.findById(id).orElseThrow(() -> new AccountTransactionException(AccountTransactionException.NOT_FOUND));
        updateAccountBalance(transaction, false);
        accountTransactionRepository.deleteById(id);
    }

    public List<AccountTransaction> getAllIncomes(Long accountId) throws AccountException {
        accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        return accountTransactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == AccountTransactionType.INCOME)
                .toList();
    }

    private void updateAccountBalance(AccountTransaction transaction, boolean isNewTransaction) throws AccountException {
        Account account = accountRepository.findById(transaction.getUser().getAccount().getId()).orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        BigDecimal mainBalance = account.getMainBalance();
        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getPrice());
        if (transaction.getTransactionType() == AccountTransactionType.INCOME) {
            mainBalance = isNewTransaction ? mainBalance.add(transactionValue) : mainBalance.subtract(transactionValue);
        } else if (transaction.getTransactionType() == AccountTransactionType.EXPENSE) {
            mainBalance = isNewTransaction ? mainBalance.subtract(transactionValue) : mainBalance.add(transactionValue);
        }
        account.setMainBalance(mainBalance);
        accountRepository.save(account);
    }
}

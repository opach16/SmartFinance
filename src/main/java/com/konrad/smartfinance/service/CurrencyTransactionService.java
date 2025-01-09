package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.CurrencyTransactionType;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.CurrencyTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.AccountRepository;
import com.konrad.smartfinance.repository.CurrencyRepository;
import com.konrad.smartfinance.repository.CurrencyTransactionRepository;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;

    public List<CurrencyTransaction> getAllTransactions() {
        return currencyTransactionRepository.findAll();
    }

    public CurrencyTransaction getTransactionById(Long id) throws CurrencyTransactionException {
        return currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
    }

    public List<CurrencyTransaction> getTransactionByUserId(Long userId) throws UserException {
        userRepository.findById(userId).orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        return currencyTransactionRepository.findByUserId(userId);
    }

    public CurrencyTransaction addTransaction(CurrencyTransactionRequest request) throws UserException, CurrencyExeption, AccountException {
        System.out.println(request.getUserId());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        Currency currency = currencyRepository.findBySymbol(request.getCurrency())
                .orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        CurrencyTransaction transaction = CurrencyTransaction.builder()
                .user(user)
                .currencyTransactionType(request.getTransactionType())
                .currency(currency)
                .amount(request.getAmount())
                .price(request.getPrice())
                .transactionDate(request.getTransactionDate())
                .build();
        CurrencyTransaction savedTransaction = currencyTransactionRepository.save(transaction);
        updateAccountBalance(savedTransaction, true);
        return savedTransaction;
    }

    public CurrencyTransaction updateTransaction(Long id, CurrencyTransactionRequest request) throws CurrencyTransactionException, CurrencyExeption, AccountException {
        CurrencyTransaction transaction = currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        Currency currency = currencyRepository.findBySymbol(request.getCurrency())
                .orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        updateAccountBalance(transaction, false);
        transaction.setCurrencyTransactionType(request.getTransactionType());
        transaction.setCurrency(currency);
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        CurrencyTransaction updatedTransaction = currencyTransactionRepository.save(transaction);
        updateAccountBalance(updatedTransaction, true);
        return updatedTransaction;
    }

    public void deleteTransaction(Long id) throws CurrencyTransactionException, AccountException {
        CurrencyTransaction transaction = currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        currencyTransactionRepository.deleteById(id);
        updateAccountBalance(transaction, false);
    }

    private void updateAccountBalance(CurrencyTransaction transaction, boolean isNewTransaction) throws AccountException {
        Account account = accountRepository.findById(transaction.getUser().getAccount().getId())
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        BigDecimal assetsBalance = account.getAssetsBalance();
        BigDecimal mainBalance = account.getMainBalance();
        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getPrice());
        BigDecimal currentValue = transaction.getAmount().multiply(transaction.getCurrency().getPrice());
        if (transaction.getCurrencyTransactionType() == CurrencyTransactionType.BUY) {
            assetsBalance = isNewTransaction ? assetsBalance.add(currentValue) : assetsBalance.subtract(currentValue);
            mainBalance = isNewTransaction ? mainBalance.subtract(transactionValue) : mainBalance.add(transactionValue);
        } else if (transaction.getCurrencyTransactionType() == CurrencyTransactionType.SELL) {
            assetsBalance = isNewTransaction ? assetsBalance.subtract(currentValue) : assetsBalance.add(currentValue);
            mainBalance = isNewTransaction ? mainBalance.add(transactionValue) : mainBalance.subtract(transactionValue);
        }
        account.setAssetsBalance(assetsBalance);
        account.setMainBalance(mainBalance);
        accountRepository.save(account);
    }
}
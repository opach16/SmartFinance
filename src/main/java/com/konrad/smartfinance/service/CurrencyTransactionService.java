package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.CurrencyTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.CurrencyRepository;
import com.konrad.smartfinance.repository.CurrencyTransactionRepository;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

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

    public CurrencyTransaction addTransaction(CurrencyTransactionRequest request) throws UserException, CurrencyExeption {
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
        return currencyTransactionRepository.save(transaction);
    }

    public CurrencyTransaction updateTransaction(Long id, CurrencyTransactionRequest request) throws CurrencyTransactionException, CurrencyExeption {
        CurrencyTransaction transaction = currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        Currency currency = currencyRepository.findBySymbol(request.getCurrency())
                .orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        transaction.setCurrencyTransactionType(request.getTransactionType());
        transaction.setCurrency(currency);
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        return currencyTransactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) throws CurrencyTransactionException {
        currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        currencyTransactionRepository.deleteById(id);
    }

}
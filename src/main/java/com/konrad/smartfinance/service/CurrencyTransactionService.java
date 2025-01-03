package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CurrencyTransactionException;
import com.konrad.smartfinance.repository.CurrencyTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;

    public List<CurrencyTransaction> getAllTransactions() {
        return currencyTransactionRepository.findAll();
    }

    public CurrencyTransaction getTransactionById(Long id) throws CurrencyTransactionException {
        return currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
    }

    public List<CurrencyTransaction> getTransactionByUserId(Long id) throws CurrencyTransactionException {
        return currencyTransactionRepository.findByUserId(id);
    }

    public CurrencyTransaction addTransaction(CurrencyTransaction currencyTransaction) {
        return currencyTransactionRepository.save(currencyTransaction);
    }

    public CurrencyTransaction updateTransaction(Long id, CurrencyTransaction currencyTransaction) throws CurrencyTransactionException {
        CurrencyTransaction fetchedTransaction = currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        fetchedTransaction.setUser(currencyTransaction.getUser());
        fetchedTransaction.setCurrency(currencyTransaction.getCurrency());
        fetchedTransaction.setAmount(currencyTransaction.getAmount());
        fetchedTransaction.setPrice(currencyTransaction.getPrice());
        fetchedTransaction.setTransactionDate(currencyTransaction.getTransactionDate());
        return currencyTransactionRepository.save(fetchedTransaction);
    }

    public void deleteTransaction(Long id) throws CurrencyTransactionException {
        currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        currencyTransactionRepository.deleteById(id);
    }

}
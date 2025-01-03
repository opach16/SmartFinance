package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.exception.CryptoTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.CryptoTransactionRepository;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoTransactionService {

    private final CryptoTransactionRepository cryptoTransactionRepository;
    private final UserRepository userRepository;

    public List<CryptoTransaction> getAllTransactions() {
        return cryptoTransactionRepository.findAll();
    }

    public List<CryptoTransaction> getTransactionsByUserId(Long userId) throws UserException {
        userRepository.findById(userId).orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        return cryptoTransactionRepository.findByUserId(userId);
    }

    public CryptoTransaction getTransactionById(Long id) throws CryptoTransactionException {
        return cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
    }

    public CryptoTransaction addTransaction(CryptoTransaction cryptoTransaction) {
        return cryptoTransactionRepository.save(cryptoTransaction);
    }

    public CryptoTransaction updateTransaction(Long id, CryptoTransaction cryptoTransaction) throws CryptoTransactionException {
        CryptoTransaction fetchedTransaction = cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        fetchedTransaction.setUser(cryptoTransaction.getUser());
        fetchedTransaction.setCryptocurrency(cryptoTransaction.getCryptocurrency());
        fetchedTransaction.setCryptoTransactionType(cryptoTransaction.getCryptoTransactionType());
        fetchedTransaction.setAmount(cryptoTransaction.getAmount());
        fetchedTransaction.setPrice(cryptoTransaction.getPrice());
        fetchedTransaction.setTransactionDate(cryptoTransaction.getTransactionDate());
        return cryptoTransactionRepository.save(fetchedTransaction);
    }

    public void deleteTransaction(Long id) throws CryptoTransactionException {
        cryptoTransactionRepository.findById(id).orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        cryptoTransactionRepository.deleteById(id);
    }
}

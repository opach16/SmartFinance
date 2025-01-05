package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CryptoTransactionException;
import com.konrad.smartfinance.exception.CryptocurrencyException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.CryptoTransactionRepository;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoTransactionService {

    private final CryptoTransactionRepository cryptoTransactionRepository;
    private final UserRepository userRepository;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    public List<CryptoTransaction> getAllTransactions() {
        return cryptoTransactionRepository.findAll();
    }

    public CryptoTransaction getTransactionById(Long id) throws CryptoTransactionException {
        return cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
    }

    public List<CryptoTransaction> getTransactionsByUserId(Long userId) throws UserException {
        userRepository.findById(userId).orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        return cryptoTransactionRepository.findByUserId(userId);
    }

    public CryptoTransaction addTransaction(CryptoTransactionRequest request) throws UserException, CryptocurrencyException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(request.getCryptocurrency())
                .orElseThrow(() -> new CryptocurrencyException(CryptocurrencyException.NOT_FOUND));
        CryptoTransaction transaction = CryptoTransaction.builder()
                .user(user)
                .cryptoTransactionType(request.getTransactionType())
                .cryptocurrency(cryptocurrency)
                .amount(request.getAmount())
                .price(request.getPrice())
                .transactionDate(request.getTransactionDate())
                .build();
        return cryptoTransactionRepository.save(transaction);
    }

    public CryptoTransaction updateTransaction(Long id, CryptoTransactionRequest request) throws CryptoTransactionException, CryptocurrencyException {
        CryptoTransaction transaction = cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(request.getCryptocurrency())
                .orElseThrow(() -> new CryptocurrencyException(CryptocurrencyException.NOT_FOUND));
        transaction.setCryptoTransactionType(request.getTransactionType());
        transaction.setCryptocurrency(cryptocurrency);
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        return cryptoTransactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) throws CryptoTransactionException {
        cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        cryptoTransactionRepository.deleteById(id);
    }
}

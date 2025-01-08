package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.CryptoTransactionException;
import com.konrad.smartfinance.exception.CryptocurrencyException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.AccountRepository;
import com.konrad.smartfinance.repository.CryptoTransactionRepository;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoTransactionService {

    private final CryptoTransactionRepository cryptoTransactionRepository;
    private final UserRepository userRepository;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final AccountRepository accountRepository;

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

    public CryptoTransaction addTransaction(CryptoTransactionRequest request) throws UserException, CryptocurrencyException, AccountException {
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
        CryptoTransaction savedTransaction = cryptoTransactionRepository.save(transaction);
        updateAccountBalance(savedTransaction, true);
        return savedTransaction;
    }

    public CryptoTransaction updateTransaction(Long id, CryptoTransactionRequest request) throws CryptoTransactionException, CryptocurrencyException, AccountException {
        CryptoTransaction transaction = cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(request.getCryptocurrency())
                .orElseThrow(() -> new CryptocurrencyException(CryptocurrencyException.NOT_FOUND));
        updateAccountBalance(transaction, false);
        transaction.setCryptoTransactionType(request.getTransactionType());
        transaction.setCryptocurrency(cryptocurrency);
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        CryptoTransaction updatedTransaction = cryptoTransactionRepository.save(transaction);
        updateAccountBalance(updatedTransaction, true);
        return updatedTransaction;
    }

    public void deleteTransaction(Long id) throws CryptoTransactionException, AccountException {
        CryptoTransaction transaction = cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        cryptoTransactionRepository.deleteById(id);
        updateAccountBalance(transaction, false);
    }

    private void updateAccountBalance(CryptoTransaction transaction, boolean isNewTransaction) throws AccountException {
        Account account = accountRepository.findById(transaction.getUser().getId())
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        BigDecimal assetsBalance = account.getAssetsBalance();
        BigDecimal mainBalance = account.getMainBalance();
        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getPrice());
        BigDecimal currentValue = transaction.getAmount().multiply(transaction.getCryptocurrency().getPrice());
        if (transaction.getCryptoTransactionType() == CryptoTransactionType.BUY) {
            assetsBalance = isNewTransaction ? assetsBalance.add(currentValue) : assetsBalance.subtract(currentValue);
            mainBalance = isNewTransaction ? mainBalance.subtract(transactionValue) : mainBalance.add(transactionValue);
        } else if (transaction.getCryptoTransactionType() == CryptoTransactionType.SELL) {
            assetsBalance = isNewTransaction ? assetsBalance.subtract(currentValue) : assetsBalance.add(currentValue);
            mainBalance = isNewTransaction ? mainBalance.add(transactionValue) : mainBalance.subtract(transactionValue);
        }
        account.setAssetsBalance(assetsBalance);
        account.setMainBalance(mainBalance);
        accountRepository.save(account);
    }
}

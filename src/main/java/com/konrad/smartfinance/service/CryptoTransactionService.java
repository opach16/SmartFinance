package com.konrad.smartfinance.service;

import com.konrad.smartfinance.Logger;
import com.konrad.smartfinance.domain.AssetType;
import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.*;
import com.konrad.smartfinance.exception.*;
import com.konrad.smartfinance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoTransactionService {

    private final CryptoTransactionRepository cryptoTransactionRepository;
    private final UserRepository userRepository;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final AccountRepository accountRepository;
    private final AssetsService assetsService;
    private final AssetRepository assetRepository;
    private final Logger logger = Logger.getInstance();

    public List<CryptoTransaction> getAllTransactions() {
        return cryptoTransactionRepository.findAll();
    }

    public CryptoTransaction getTransactionById(Long id) throws CryptoTransactionException {
        return cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
    }

    public List<CryptoTransaction> getTransactionsByUserId(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        return cryptoTransactionRepository.findByUserId(user.getId());
    }

    public CryptoTransaction addTransaction(CryptoTransactionRequest request) throws UserException, CryptocurrencyException, AccountException, AssetException {
        verifyAssets(request);
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
        updateAssets(savedTransaction, true);
        logHighTransaction(savedTransaction);
        return savedTransaction;
    }

    public CryptoTransaction updateTransaction(CryptoTransactionRequest request) throws CryptoTransactionException, CryptocurrencyException, AccountException, AssetException, UserException {
        CryptoTransaction transaction = cryptoTransactionRepository.findById(request.getId())
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(request.getCryptocurrency())
                .orElseThrow(() -> new CryptocurrencyException(CryptocurrencyException.NOT_FOUND));
        verifyAssets(request, transaction);
        updateAccountBalance(transaction, false);
        updateAssets(transaction, false);
        transaction.setCryptoTransactionType(request.getTransactionType());
        transaction.setCryptocurrency(cryptocurrency);
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        CryptoTransaction updatedTransaction = cryptoTransactionRepository.save(transaction);
        updateAccountBalance(updatedTransaction, true);
        updateAssets(updatedTransaction, true);
        return updatedTransaction;
    }

    public void deleteTransaction(Long id) throws CryptoTransactionException, AccountException, AssetException {
        CryptoTransaction transaction = cryptoTransactionRepository.findById(id)
                .orElseThrow(() -> new CryptoTransactionException(CryptoTransactionException.NOT_FOUND));
        verifyAssets(transaction);
        cryptoTransactionRepository.deleteById(id);
        updateAccountBalance(transaction, false);
        updateAssets(transaction, false);
    }

    private void updateAccountBalance(CryptoTransaction transaction, boolean isNewTransaction) throws AccountException {
        Account account = accountRepository.findById(transaction.getUser().getAccount().getId())
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        BigDecimal assetsBalance = account.getAssetsBalance();
        BigDecimal mainBalance = account.getMainBalance();
        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getPrice());
        BigDecimal currentValue = transaction.getAmount().multiply(transaction.getCryptocurrency().getPrice()).divide(account.getMainCurrency().getPrice(), 2, RoundingMode.CEILING);
        switch (transaction.getCryptoTransactionType()) {
            case BUY -> {
                assetsBalance = isNewTransaction ? assetsBalance.add(currentValue) : assetsBalance.subtract(currentValue);
                mainBalance = isNewTransaction ? mainBalance.subtract(transactionValue) : mainBalance.add(transactionValue);
            }
            case SELL -> {
                assetsBalance = isNewTransaction ? assetsBalance.subtract(currentValue) : assetsBalance.add(currentValue);
                mainBalance = isNewTransaction ? mainBalance.add(transactionValue) : mainBalance.subtract(transactionValue);
            }
        }
        account.setAssetsBalance(assetsBalance);
        account.setMainBalance(mainBalance);
        accountRepository.save(account);
    }

    private void updateAssets(CryptoTransaction transaction, boolean isNewTransaction) {
        Asset asset = new Asset(transaction.getUser(), AssetType.CRYPTOCURRENCY, transaction.getCryptocurrency().getSymbol(), transaction.getAmount());
        switch (transaction.getCryptoTransactionType()) {
            case BUY -> {
                if (isNewTransaction) {
                    assetsService.addAsset(asset);
                } else {
                    assetsService.deleteAsset(asset);
                }
            }
            case SELL -> {
                if (isNewTransaction) {
                    assetsService.deleteAsset(asset);
                } else {
                    assetsService.addAsset(asset);
                }
            }
        }
    }

    private void verifyAssets(CryptoTransaction transaction) throws AssetException {
        Asset asset = assetRepository.findByUserAndName(transaction.getUser(), transaction.getCryptocurrency().getSymbol())
                .orElseThrow(() -> new AssetException(AssetException.NOT_FOUND));
        if (asset.getAmount().compareTo(transaction.getAmount()) < 0) {
            throw new AssetException("Insufficient assets");
        }
    }

    private void verifyAssets(CryptoTransactionRequest request) throws AssetException, UserException {
        if (request.getTransactionType() == CryptoTransactionType.SELL) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
            Asset asset = assetRepository.findByUserAndName(user, request.getCryptocurrency())
                    .orElseThrow(() -> new AssetException(AssetException.NOT_FOUND));
            if (asset.getAmount().compareTo(request.getAmount()) < 0) {
                throw new AssetException("Insufficient assets");
            }
        }
    }

    private void verifyAssets(CryptoTransactionRequest request, CryptoTransaction transaction) throws AssetException, UserException {
        if (request.getTransactionType() == CryptoTransactionType.SELL) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
            Asset asset = assetRepository.findByUserAndName(user, request.getCryptocurrency())
                    .orElseThrow(() -> new AssetException(AssetException.NOT_FOUND));
            if (asset.getAmount().subtract(transaction.getAmount()).compareTo(request.getAmount()) < 0) {
                throw new AssetException("Insufficient assets");
            }
        }
    }

    private void logHighTransaction(CryptoTransaction transaction) {
        String username = transaction.getUser().getUsername();
        String symbol = transaction.getCryptocurrency().getSymbol();
        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getPrice());
        if (transactionValue.compareTo(BigDecimal.valueOf(100000)) >= 0) {
            if (transaction.getCryptoTransactionType() == CryptoTransactionType.BUY) {
                logger.log(username + " bought " + transactionValue + " " + symbol);
            } else if (transaction.getCryptoTransactionType() == CryptoTransactionType.SELL) {
                logger.log(username + " sold " + transactionValue + " " + symbol);
            }
        }
    }
}

package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.AssetType;
import com.konrad.smartfinance.domain.CurrencyTransactionType;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
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
public class CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
    private final AssetsService assetsService;
    private final AssetRepository assetRepository;

    public List<CurrencyTransaction> getAllTransactions() {
        return currencyTransactionRepository.findAll();
    }

    public CurrencyTransaction getTransactionById(Long id) throws CurrencyTransactionException {
        return currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
    }

    public List<CurrencyTransaction> getTransactionByUserId(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        return currencyTransactionRepository.findByUserId(user.getId());
    }

    public CurrencyTransaction addTransaction(CurrencyTransactionRequest request) throws UserException, CurrencyExeption, AccountException, AssetException {
        verifyAssets(request);
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
        updateAssets(savedTransaction, true);
        return savedTransaction;
    }

    public CurrencyTransaction updateTransaction(CurrencyTransactionRequest request) throws CurrencyTransactionException, CurrencyExeption, AccountException, AssetException, UserException {
        CurrencyTransaction transaction = currencyTransactionRepository.findById(request.getId())
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        Currency currency = currencyRepository.findBySymbol(request.getCurrency())
                .orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        verifyAssets(request, transaction);
        updateAccountBalance(transaction, false);
        updateAssets(transaction, false);
        transaction.setCurrencyTransactionType(request.getTransactionType());
        transaction.setCurrency(currency);
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        CurrencyTransaction updatedTransaction = currencyTransactionRepository.save(transaction);
        updateAccountBalance(updatedTransaction, true);
        updateAssets(updatedTransaction, true);
        return updatedTransaction;
    }

    public void deleteTransaction(Long id) throws CurrencyTransactionException, AccountException, AssetException, UserException {
        CurrencyTransaction transaction = currencyTransactionRepository.findById(id)
                .orElseThrow(() -> new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND));
        verifyAssets(transaction);
        currencyTransactionRepository.deleteById(id);
        updateAccountBalance(transaction, false);
        updateAssets(transaction, false);
    }

    private void updateAccountBalance(CurrencyTransaction transaction, boolean isNewTransaction) throws AccountException {
        Account account = accountRepository.findById(transaction.getUser().getId())
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        BigDecimal assetsBalance = account.getAssetsBalance();
        BigDecimal mainBalance = account.getMainBalance();
        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getPrice());
        BigDecimal currentValue = transaction.getAmount().multiply(transaction.getCurrency().getPrice()).divide(account.getMainCurrency().getPrice(), 2, RoundingMode.CEILING);
        switch (transaction.getCurrencyTransactionType()) {
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

    private void updateAssets(CurrencyTransaction transaction, boolean isNewTransaction) throws AssetException {
        Asset asset = new Asset(transaction.getUser(), AssetType.CURRENCY, transaction.getCurrency().getSymbol(), transaction.getAmount());
        switch (transaction.getCurrencyTransactionType()) {
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

    private void verifyAssets(CurrencyTransaction transaction) throws AssetException {
        Asset asset = assetRepository.findByUserAndName(transaction.getUser(), transaction.getCurrency().getSymbol())
                .orElseThrow(() -> new AssetException(AssetException.NOT_FOUND));
        if (asset.getAmount().compareTo(transaction.getAmount()) < 0) {
            throw new AssetException("Insufficient assets");
        }
    }

    private void verifyAssets(CurrencyTransactionRequest request) throws AssetException, UserException {
        if (request.getTransactionType() == CurrencyTransactionType.SELL) {
            User user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
            Asset asset = assetRepository.findByUserAndName(user, request.getCurrency())
                    .orElseThrow(() -> new AssetException(AssetException.NOT_FOUND));
            if (asset.getAmount().compareTo(request.getAmount()) < 0) {
                throw new AssetException("Insufficient assets");
            }
        }
    }

    private void verifyAssets(CurrencyTransactionRequest request, CurrencyTransaction transaction) throws AssetException, UserException {
        if (request.getTransactionType() == CurrencyTransactionType.SELL) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
            Asset asset = assetRepository.findByUserAndName(user, request.getCurrency()).orElseThrow(() -> new AssetException(AssetException.NOT_FOUND));
            if (asset.getAmount().subtract(transaction.getAmount()).compareTo(request.getAmount()) < 0) {
                throw new AssetException("Insufficient assets");
            }
        }
    }
}
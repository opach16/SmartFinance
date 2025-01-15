package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.DebitTransactionType;
import com.konrad.smartfinance.domain.dto.DebitTransactionRequest;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.DebitTransaction;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.DebitTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.AccountRepository;
import com.konrad.smartfinance.repository.DebitTransactionRepository;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebitTransactionService {

    private final DebitTransactionRepository debitTransactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<DebitTransaction> getAllTransactions(Long accountId) throws AccountException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));

        return debitTransactionRepository.findByUser(account.getUser());
    }

    public List<DebitTransaction> getAllExpenses(Long accountId) throws AccountException {
        accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        return debitTransactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == DebitTransactionType.EXPENSE)
                .toList();
    }

    public DebitTransaction getTransactionById(Long id) throws DebitTransactionException {
        return debitTransactionRepository.findById(id)
                .orElseThrow(() -> new DebitTransactionException(DebitTransactionException.NOT_FOUND));
    }

    public DebitTransaction addTransaction(DebitTransactionRequest request, Long userId) throws UserException, AccountException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        DebitTransaction transaction = DebitTransaction.builder()
                .user(user)
                .transactionType(request.getTransactionType())
                .name(request.getName())
                .amount(request.getAmount())
                .price(request.getPrice())
                .transactionDate(request.getTransactionDate())
                .build();
        DebitTransaction savedTransaction = debitTransactionRepository.save(transaction);
        updateAccountBalance(savedTransaction, true);
        return savedTransaction;
    }

    public DebitTransaction updateTransaction(DebitTransactionRequest request) throws DebitTransactionException, AccountException {
        DebitTransaction transaction = debitTransactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new DebitTransactionException(DebitTransactionException.NOT_FOUND));
        updateAccountBalance(transaction, false);
        transaction.setTransactionType(request.getTransactionType());
        transaction.setName(request.getName());
        transaction.setAmount(request.getAmount());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());
        DebitTransaction updatedTransaction = debitTransactionRepository.save(transaction);
        updateAccountBalance(updatedTransaction, true);
        return updatedTransaction;
    }

    public void deleteTransaction(Long id) throws DebitTransactionException, AccountException {
        DebitTransaction transaction = debitTransactionRepository.findById(id).orElseThrow(() -> new DebitTransactionException(DebitTransactionException.NOT_FOUND));
        updateAccountBalance(transaction, false);
        debitTransactionRepository.deleteById(id);
    }

    public List<DebitTransaction> getAllIncomes(Long accountId) throws AccountException {
        accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        return debitTransactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == DebitTransactionType.INCOME)
                .toList();
    }

    private void updateAccountBalance(DebitTransaction transaction, boolean isNewTransaction) throws AccountException {
        Account account = accountRepository.findById(transaction.getUser().getAccount().getId()).orElseThrow(() -> new AccountException(AccountException.NOT_FOUND));
        BigDecimal mainBalance = account.getMainBalance();
        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getPrice());
        if (transaction.getTransactionType() == DebitTransactionType.INCOME) {
            mainBalance = isNewTransaction ? mainBalance.add(transactionValue) : mainBalance.subtract(transactionValue);
        } else if (transaction.getTransactionType() == DebitTransactionType.EXPENSE) {
            mainBalance = isNewTransaction ? mainBalance.subtract(transactionValue) : mainBalance.add(transactionValue);
        }
        account.setMainBalance(mainBalance);
        accountRepository.save(account);
    }
}

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebitTransactionServiceTestSuite {

    private User user;
    private Account account;
    private DebitTransaction incomeTransaction;
    private DebitTransaction expenseTransaction;

    @InjectMocks
    private DebitTransactionService debitTransactionService;

    @Mock
    private DebitTransactionRepository debitTransactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .username("testUser")
                .account(account)
                .build();
        account = Account.builder()
                .id(1L)
                .user(user)
                .mainBalance(new BigDecimal("1000"))
                .build();
        user.setAccount(account);
        incomeTransaction = DebitTransaction.builder()
                .id(1L)
                .user(user)
                .transactionType(DebitTransactionType.INCOME)
                .name("testIncomeName")
                .amount(BigDecimal.ONE)
                .price(new BigDecimal("500"))
                .transactionDate(LocalDate.of(2021, 1, 1))
                .build();
        expenseTransaction = DebitTransaction.builder()
                .id(2L)
                .user(user)
                .transactionType(DebitTransactionType.EXPENSE)
                .name("testExpenseName")
                .amount(new BigDecimal("200"))
                .price(BigDecimal.ONE)
                .transactionDate(LocalDate.of(2022, 2, 2))
                .build();
    }

    @Test
    void shouldGetAllTransactions() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(debitTransactionRepository.findByUser(user)).thenReturn(List.of(incomeTransaction, expenseTransaction));
        //when
        List<DebitTransaction> transactions = debitTransactionService.getAllTransactions(1L);
        //then
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        verify(userRepository, times(1)).findById(1L);
        verify(debitTransactionRepository, times(1)).findByUser(user);
    }

    @Test
    void shouldGetAllExpenses() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(debitTransactionRepository.findAll()).thenReturn(List.of(incomeTransaction, expenseTransaction));
        //when
        List<DebitTransaction> expenses = debitTransactionService.getAllExpenses(1L);
        //then
        assertNotNull(expenses);
        assertEquals(1, expenses.size());
        assertEquals(expenseTransaction.getName(), expenses.getFirst().getName());
        verify(userRepository, times(1)).findById(1L);
        verify(debitTransactionRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAllIncomes() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(debitTransactionRepository.findAll()).thenReturn(List.of(incomeTransaction, expenseTransaction));
        //when
        List<DebitTransaction> incomes = debitTransactionService.getAllIncomes(1L);
        //then
        assertNotNull(incomes);
        assertEquals(1, incomes.size());
        assertEquals(incomeTransaction.getName(), incomes.getFirst().getName());
        verify(userRepository, times(1)).findById(1L);
        verify(debitTransactionRepository, times(1)).findAll();
    }

    @Test
    void shouldAddTransaction() throws UserException, AccountException {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(1L)
                .transactionType(DebitTransactionType.INCOME)
                .name("testIncomeName")
                .amount(BigDecimal.ONE)
                .price(new BigDecimal("100"))
                .transactionDate(LocalDate.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(debitTransactionRepository.save(any(DebitTransaction.class))).thenReturn(incomeTransaction);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        //when
        DebitTransaction transaction = debitTransactionService.addTransaction(request);
        //then
        assertNotNull(transaction);
        assertEquals("testIncomeName", transaction.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(debitTransactionRepository, times(1)).save(any(DebitTransaction.class));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnAddTransaction() {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(2L)
                .build();
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> debitTransactionService.addTransaction(request));
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void shouldUpdateTransaction() throws DebitTransactionException, AccountException {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .transactionId(1L)
                .transactionType(DebitTransactionType.EXPENSE)
                .name("Updated Expense")
                .amount(new BigDecimal("50"))
                .price(BigDecimal.ONE)
                .transactionDate(LocalDate.now())
                .build();

        when(debitTransactionRepository.findById(1L)).thenReturn(Optional.of(incomeTransaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(debitTransactionRepository.save(any(DebitTransaction.class))).thenReturn(expenseTransaction);
        //when
        DebitTransaction updatedTransaction = debitTransactionService.updateTransaction(request);
        //then
        assertNotNull(updatedTransaction);
        assertEquals(expenseTransaction.getName(), updatedTransaction.getName());
        verify(debitTransactionRepository, times(1)).findById(1L);
        verify(accountRepository, times(2)).findById(1L);
        verify(debitTransactionRepository, times(1)).save(any(DebitTransaction.class));
    }

    @Test
    void shouldDeleteTransaction() throws DebitTransactionException, AccountException {
        //given
        when(debitTransactionRepository.findById(1L)).thenReturn(Optional.of(incomeTransaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        //when
        debitTransactionService.deleteTransaction(1L);
        //then
        verify(debitTransactionRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(1L);
        verify(debitTransactionRepository, times(1)).deleteById(1L);
        verify(accountRepository, times(1)).save(account);
    }
}
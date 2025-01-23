package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.AssetType;
import com.konrad.smartfinance.domain.CurrencyTransactionType;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.*;
import com.konrad.smartfinance.exception.*;
import com.konrad.smartfinance.repository.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyTransactionServiceTestSuite {

    Currency currency1;
    Currency currency2;
    User user;
    Account account;
    Asset asset;


    @InjectMocks
    private CurrencyTransactionService currencyTransactionService;

    @Mock
    private CurrencyTransactionRepository currencyTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetsService assetsService;

    @BeforeEach
    void setUp() {
        currency1 = Currency.builder()
                .id(1L)
                .symbol("testSymbol1")
                .price(BigDecimal.valueOf(50000))
                .build();
        currency2 = Currency.builder()
                .id(2L)
                .symbol("testSymbol2")
                .price(BigDecimal.TWO)
                .build();
        user = User.builder()
                .id(1L)
                .build();
        account = Account.builder()
                .id(1L)
                .user(user)
                .mainCurrency(currency2)
                .mainBalance(BigDecimal.valueOf(1000))
                .assetsBalance(BigDecimal.ZERO)
                .build();
        user.setAccount(account);
        asset = Asset.builder()
                .id(1L)
                .user(user)
                .assetType(AssetType.CRYPTOCURRENCY)
                .amount(BigDecimal.TEN)
                .name(currency1.getSymbol())
                .build();
    }

    @Test
    void shouldGetAllTransactions() {
        //given
        CurrencyTransaction transaction = new CurrencyTransaction();
        when(currencyTransactionRepository.findAll()).thenReturn(List.of(transaction));
        //when
        List<CurrencyTransaction> result = currencyTransactionService.getAllTransactions();
        //then
        assertEquals(1, result.size());
        verify(currencyTransactionRepository, times(1)).findAll();
    }

    @Test
    void shouldGetTransactionById() throws CurrencyTransactionException {
        //given
        CurrencyTransaction transaction = new CurrencyTransaction();
        when(currencyTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        //when
        CurrencyTransaction result = currencyTransactionService.getTransactionById(1L);
        //then
        assertNotNull(result);
        verify(currencyTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFoundOnGetTransactionById() {
        //given
        when(currencyTransactionRepository.findById(1L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(CurrencyTransactionException.class, () -> currencyTransactionService.getTransactionById(1L));
    }

    @Test
    void shouldAddTransaction() throws UserException, CurrencyExeption, AccountException, AssetException {
        //given
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CurrencyTransactionType.BUY)
                .currency(currency1.getSymbol())
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        CurrencyTransaction transaction = CurrencyTransaction.builder()
                .id(1L)
                .user(user)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .currency(currency1)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(currencyRepository.findBySymbol(currency1.getSymbol())).thenReturn(Optional.of(currency1));
        when(currencyTransactionRepository.save(any(CurrencyTransaction.class))).thenReturn(transaction);
        //when
        CurrencyTransaction result = currencyTransactionService.addTransaction(request);
        //then
        assertNotNull(result);
        verify(currencyTransactionRepository, times(1)).save(any(CurrencyTransaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnAddTransaction() {
        //given
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CurrencyTransactionType.BUY)
                .currency("testSymbol")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        //then
        assertThrows(UserException.class, () -> currencyTransactionService.addTransaction(request));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotFoundOnAddTransaction() {
        //given
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CurrencyTransactionType.BUY)
                .currency("testSymbol1")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(currencyRepository.findBySymbol(request.getCurrency())).thenReturn(Optional.empty());
        //then
        assertThrows(CurrencyExeption.class, () -> currencyTransactionService.addTransaction(request));
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundOnAddTransaction() {
        //given
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CurrencyTransactionType.BUY)
                .currency("testSymbol1")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        CurrencyTransaction transaction = CurrencyTransaction.builder()
                .id(1L)
                .user(user)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .currency(currency1)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(currencyRepository.findBySymbol(currency1.getSymbol())).thenReturn(Optional.of(currency1));
        when(currencyTransactionRepository.save(any(CurrencyTransaction.class))).thenReturn(transaction);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        //then
        assertThrows(AccountException.class, () -> currencyTransactionService.addTransaction(request));
    }

    @Test
    void shouldThrowExceptionWhenAssetNotFoundOnAddTransaction() {
        //given
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CurrencyTransactionType.SELL)
                .currency("testSymbol1")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(assetRepository.findByUserAndName(user, request.getCurrency())).thenReturn(Optional.empty());
        //then
        assertThrows(AssetException.class, () -> currencyTransactionService.addTransaction(request));
    }

    @Test
    void shouldUpdateTransaction() throws Exception {
        //given
        CurrencyTransaction transaction = CurrencyTransaction.builder()
                .id(1L)
                .user(user)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .currency(currency1)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        CurrencyTransactionRequest request = CurrencyTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CurrencyTransactionType.SELL)
                .currency(currency1.getSymbol())
                .amount(BigDecimal.valueOf(0.5))
                .price(BigDecimal.TWO)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(assetRepository.findByUserAndName(user, request.getCurrency())).thenReturn(Optional.of(asset));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(currencyTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(currencyRepository.findBySymbol(currency1.getSymbol())).thenReturn(Optional.of(currency1));
        when(currencyTransactionRepository.save(any(CurrencyTransaction.class))).thenReturn(transaction);
        //when
        CurrencyTransaction result = currencyTransactionService.updateTransaction(request);
        //then
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getTransactionType(), result.getCurrencyTransactionType());
        assertEquals(request.getCurrency(), result.getCurrency().getSymbol());
        assertEquals(request.getAmount(), result.getAmount());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getTransactionDate(), result.getTransactionDate());
        verify(currencyTransactionRepository, times(1)).save(transaction);
    }

    @Test
    void shouldDeleteTransaction() throws CurrencyTransactionException, AccountException, AssetException {
        //given
        CurrencyTransaction transaction = CurrencyTransaction.builder()
                .id(1L)
                .user(user)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .currency(currency1)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        when(currencyTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(assetRepository.findByUserAndName(user, asset.getName())).thenReturn(Optional.of(asset));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        //when
        currencyTransactionService.deleteTransaction(1L);
        //then
        verify(currencyTransactionRepository, times(1)).deleteById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFoundOnDeleteTransaction() {
        //given
        when(currencyTransactionRepository.findById(1L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(CurrencyTransactionException.class, () -> currencyTransactionService.deleteTransaction(1L));
    }

}
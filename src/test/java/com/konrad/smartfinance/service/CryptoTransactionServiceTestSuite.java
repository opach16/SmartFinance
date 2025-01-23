package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.AssetType;
import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoTransactionServiceTestSuite {

    Cryptocurrency crypto;
    Currency currency;
    User user;
    Account account;
    Asset asset;


    @InjectMocks
    private CryptoTransactionService cryptoTransactionService;

    @Mock
    private CryptoTransactionRepository cryptoTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CryptocurrencyRepository cryptocurrencyRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetsService assetsService;

    @BeforeEach
    void setUp() {
        crypto = Cryptocurrency.builder()
                .id(1L)
                .symbol("testSymbol")
                .name("testName")
                .price(BigDecimal.valueOf(50000))
                .build();
        currency = Currency.builder()
                .id(1L)
                .symbol("testSymbol")
                .price(BigDecimal.TWO)
                .build();
        user = User.builder()
                .id(1L)
                .build();
        account = Account.builder()
                .id(1L)
                .user(user)
                .mainCurrency(currency)
                .mainBalance(BigDecimal.valueOf(1000))
                .assetsBalance(BigDecimal.ZERO)
                .build();
        user.setAccount(account);
        asset = Asset.builder()
                .id(1L)
                .user(user)
                .assetType(AssetType.CRYPTOCURRENCY)
                .amount(BigDecimal.TEN)
                .name(crypto.getSymbol())
                .build();
    }

    @Test
    void shouldGetAllTransactions() {
        //given
        CryptoTransaction transaction = new CryptoTransaction();
        when(cryptoTransactionRepository.findAll()).thenReturn(List.of(transaction));
        //when
        List<CryptoTransaction> result = cryptoTransactionService.getAllTransactions();
        //then
        assertEquals(1, result.size());
        verify(cryptoTransactionRepository, times(1)).findAll();
    }

    @Test
    void shouldGetTransactionById() throws CryptoTransactionException {
        //given
        CryptoTransaction transaction = new CryptoTransaction();
        when(cryptoTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        //when
        CryptoTransaction result = cryptoTransactionService.getTransactionById(1L);
        //then
        assertNotNull(result);
        verify(cryptoTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFoundOnGetTransactionById() {
        //given
        when(cryptoTransactionRepository.findById(1L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(CryptoTransactionException.class, () -> cryptoTransactionService.getTransactionById(1L));
    }

    @Test
    void shouldAddTransaction() throws UserException, CryptocurrencyException, AccountException, AssetException {
        //given
        CryptoTransactionRequest request = CryptoTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CryptoTransactionType.BUY)
                .cryptocurrency(crypto.getSymbol())
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        CryptoTransaction transaction = CryptoTransaction.builder()
                .id(1L)
                .user(user)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .cryptocurrency(crypto)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(cryptocurrencyRepository.findBySymbol(crypto.getSymbol())).thenReturn(Optional.of(crypto));
        when(cryptoTransactionRepository.save(any(CryptoTransaction.class))).thenReturn(transaction);
        //when
        CryptoTransaction result = cryptoTransactionService.addTransaction(request);
        //then
        assertNotNull(result);
        verify(cryptoTransactionRepository, times(1)).save(any(CryptoTransaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnAddTransaction() {
        //given
        CryptoTransactionRequest request = CryptoTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CryptoTransactionType.BUY)
                .cryptocurrency("testSymbol")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        //then
        assertThrows(UserException.class, () -> cryptoTransactionService.addTransaction(request));
    }

    @Test
    void shouldThrowExceptionWhenCryptocurrencyNotFoundOnAddTransaction() {
        //given
        CryptoTransactionRequest request = CryptoTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CryptoTransactionType.BUY)
                .cryptocurrency("testSymbol")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cryptocurrencyRepository.findBySymbol(request.getCryptocurrency())).thenReturn(Optional.empty());
        //then
        assertThrows(CryptocurrencyException.class, () -> cryptoTransactionService.addTransaction(request));
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundOnAddTransaction() {
        //given
        CryptoTransactionRequest request = CryptoTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CryptoTransactionType.BUY)
                .cryptocurrency("testSymbol")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        CryptoTransaction transaction = CryptoTransaction.builder()
                .id(1L)
                .user(user)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .cryptocurrency(crypto)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cryptocurrencyRepository.findBySymbol(crypto.getSymbol())).thenReturn(Optional.of(crypto));
        when(cryptoTransactionRepository.save(any(CryptoTransaction.class))).thenReturn(transaction);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        //then
        assertThrows(AccountException.class, () -> cryptoTransactionService.addTransaction(request));
    }

    @Test
    void shouldThrowExceptionWhenAssetNotFoundOnAddTransaction() {
        //given
        CryptoTransactionRequest request = CryptoTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CryptoTransactionType.SELL)
                .cryptocurrency("testSymbol")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.valueOf(50000))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(assetRepository.findByUserAndName(user, request.getCryptocurrency())).thenReturn(Optional.empty());
        //then
        assertThrows(AssetException.class, () -> cryptoTransactionService.addTransaction(request));
    }

    @Test
    void shouldUpdateTransaction() throws Exception {
        //given
        CryptoTransaction transaction = CryptoTransaction.builder()
                .id(1L)
                .user(user)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .cryptocurrency(crypto)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        CryptoTransactionRequest request = CryptoTransactionRequest.builder()
                .id(1L)
                .userId(user.getId())
                .transactionType(CryptoTransactionType.SELL)
                .cryptocurrency(crypto.getSymbol())
                .amount(BigDecimal.valueOf(0.5))
                .price(BigDecimal.TWO)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(assetRepository.findByUserAndName(user, request.getCryptocurrency())).thenReturn(Optional.of(asset));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(cryptoTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(cryptocurrencyRepository.findBySymbol(crypto.getSymbol())).thenReturn(Optional.of(crypto));
        when(cryptoTransactionRepository.save(any(CryptoTransaction.class))).thenReturn(transaction);
        //when
        CryptoTransaction result = cryptoTransactionService.updateTransaction(request);
        //then
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getTransactionType(), result.getCryptoTransactionType());
        assertEquals(request.getCryptocurrency(), result.getCryptocurrency().getSymbol());
        assertEquals(request.getAmount(), result.getAmount());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getTransactionDate(), result.getTransactionDate());
        verify(cryptoTransactionRepository, times(1)).save(transaction);
    }

    @Test
    void shouldDeleteTransaction() throws CryptoTransactionException, AccountException, AssetException {
        //given
        CryptoTransaction transaction = CryptoTransaction.builder()
                .id(1L)
                .user(user)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .cryptocurrency(crypto)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        when(cryptoTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(assetRepository.findByUserAndName(user, asset.getName())).thenReturn(Optional.of(asset));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        //when
        cryptoTransactionService.deleteTransaction(1L);
        //then
        verify(cryptoTransactionRepository, times(1)).deleteById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFoundOnDeleteTransaction() {
        //given
        when(cryptoTransactionRepository.findById(1L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(CryptoTransactionException.class, () -> cryptoTransactionService.deleteTransaction(1L));
    }
}
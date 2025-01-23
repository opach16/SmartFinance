package com.konrad.smartfinance.service;

import com.konrad.smartfinance.client.CoingeckoClient;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CryptocurrencyException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import com.konrad.smartfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptocurrencyServiceTestSuite {

    private Cryptocurrency btc;
    private Cryptocurrency eth;
    private Currency mainCurrency;
    private User user;

    @InjectMocks
    private CryptocurrencyService cryptocurrencyService;

    @Mock
    private CryptocurrencyRepository cryptocurrencyRepository;

    @Mock
    private CoingeckoClient coingeckoClient;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        btc = Cryptocurrency.builder()
                .id(1L)
                .symbol("btc")
                .name("Bitcoin")
                .price(new BigDecimal("50000"))
                .build();

        eth = Cryptocurrency.builder()
                .id(2L)
                .symbol("eth")
                .name("Ethereum")
                .price(new BigDecimal("4000"))
                .build();
        mainCurrency = Currency.builder()
                .id(1L)
                .symbol("USD")
                .price(BigDecimal.TWO)
                .build();
        user = User.builder()
                .id(1L)
                .username("testUser")
                .build();
        Account account = Account.builder()
                .id(1L)
                .user(user)
                .mainCurrency(mainCurrency)
                .build();
        user.setAccount(account);
    }

    @Test
    void shouldGetAllCryptocurrencies() {
        //given
        when(cryptocurrencyRepository.findAll()).thenReturn(List.of(btc, eth));
        //when
        List<Cryptocurrency> fetchedList = cryptocurrencyService.getAll();
        //then
        assertNotNull(fetchedList);
        assertEquals(2, fetchedList.size());
        assertEquals(btc.getSymbol(), fetchedList.getFirst().getSymbol());
        verify(cryptocurrencyRepository, times(1)).findAll();
    }

    @Test
    void shouldGetCryptocurrencyBySymbol() throws CryptocurrencyException {
        //given
        when(cryptocurrencyRepository.findBySymbol(btc.getSymbol())).thenReturn(Optional.of(btc));
        //when
        Cryptocurrency fetchCrypto = cryptocurrencyService.getBySymbol(btc.getSymbol());
        //then
        assertNotNull(fetchCrypto);
        assertEquals(btc.getSymbol(), fetchCrypto.getSymbol());
        verify(cryptocurrencyRepository, times(1)).findBySymbol(btc.getSymbol());
    }

    @Test
    void shouldThrowExceptionWhenCryptocurrencyNotFound() {
        //given
        when(cryptocurrencyRepository.findBySymbol("invalidSymbol")).thenReturn(Optional.empty());
        //when & then
        assertThrows(CryptocurrencyException.class, () -> cryptocurrencyService.getBySymbol("invalidSymbol"));
        verify(cryptocurrencyRepository, times(1)).findBySymbol("invalidSymbol");
    }

    @Test
    void shouldGetCryptocurrencyPrice() throws CryptocurrencyException {
        //given
        when(cryptocurrencyRepository.findBySymbol(btc.getSymbol())).thenReturn(Optional.of(btc));
        //when
        BigDecimal price = cryptocurrencyService.getPrice(btc.getSymbol());
        //then
        assertEquals(btc.getPrice(), price);
        verify(cryptocurrencyRepository, times(1)).findBySymbol(btc.getSymbol());
    }

    @Test
    void shouldUpdateCryptocurrencies() {
        //given
        Cryptocurrency updatedBtc = Cryptocurrency.builder()
                .symbol(btc.getSymbol())
                .price(new BigDecimal("60000"))
                .build();
        when(coingeckoClient.fetchCryptocurrencies()).thenReturn(List.of(updatedBtc));
        when(cryptocurrencyRepository.findBySymbol(btc.getSymbol())).thenReturn(Optional.of(btc));
        //when
        cryptocurrencyService.updateCryptocurrencies();
        //then
        verify(cryptocurrencyRepository, times(1)).save(btc);
        assertEquals(new BigDecimal("60000"), btc.getPrice());
    }

    @Test
    void shouldGetAllCryptocurrenciesByUserId() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cryptocurrencyRepository.findAll()).thenReturn(List.of(btc, eth));
        //when
        List<Cryptocurrency> result = cryptocurrencyService.getAllCryptocurrenciesByUserId(1L);
        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(btc.getSymbol(), result.getFirst().getSymbol());
        assertEquals(btc.getPrice().divide(mainCurrency.getPrice(), 2, RoundingMode.CEILING), result.getFirst().getPrice());
        assertEquals(eth.getSymbol(), result.getLast().getSymbol());
        assertEquals(eth.getPrice().divide(mainCurrency.getPrice(), 2, RoundingMode.CEILING), result.getLast().getPrice());
        verify(userRepository, times(1)).findById(1L);
        verify(cryptocurrencyRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForCryptocurrencies() {
        //given
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> cryptocurrencyService.getAllCryptocurrenciesByUserId(2L));
        verify(userRepository, times(1)).findById(2L);
    }
}
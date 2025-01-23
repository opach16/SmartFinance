package com.konrad.smartfinance.service;

import com.konrad.smartfinance.client.CurrencyapiClient;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.CurrencyRepository;
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
class CurrencyServiceTestSuite {

    private Currency usd;
    private Currency pln;
    private Currency mainCurrency;
    private User user;

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyapiClient currencyapiClient;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mainCurrency = Currency.builder()
                .id(1L)
                .symbol("EUR")
                .price(BigDecimal.ONE)
                .build();
        usd = Currency.builder()
                .id(2L)
                .symbol("usd")
                .price(new BigDecimal("0.95"))
                .build();
        pln = Currency.builder()
                .id(3L)
                .symbol("pln")
                .price(new BigDecimal("0.25"))
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
    void shouldGetAllCurrencies() {
        //given
        when(currencyRepository.findAll()).thenReturn(List.of(usd, pln));
        //when
        List<Currency> fetchedList = currencyService.getAllCurrencies();
        //then
        assertNotNull(fetchedList);
        assertEquals(2, fetchedList.size());
        assertEquals(usd.getSymbol(), fetchedList.getFirst().getSymbol());
        verify(currencyRepository, times(1)).findAll();
    }

    @Test
    void shouldGetCurrencyBySymbol() throws CurrencyExeption {
        //given
        when(currencyRepository.findBySymbol(usd.getSymbol())).thenReturn(Optional.of(usd));
        //when
        Currency fetchCrypto = currencyService.getCurrencyBySymbol(usd.getSymbol());
        //then
        assertNotNull(fetchCrypto);
        assertEquals(usd.getSymbol(), fetchCrypto.getSymbol());
        verify(currencyRepository, times(1)).findBySymbol(usd.getSymbol());
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotFound() {
        //given
        when(currencyRepository.findBySymbol("invalidSymbol")).thenReturn(Optional.empty());
        //when & then
        assertThrows(CurrencyExeption.class, () -> currencyService.getCurrencyBySymbol("invalidSymbol"));
        verify(currencyRepository, times(1)).findBySymbol("invalidSymbol");
    }

    @Test
    void shouldGetCurrencyPrice() throws CurrencyExeption {
        //given
        when(currencyRepository.findBySymbol(usd.getSymbol())).thenReturn(Optional.of(usd));
        //when
        BigDecimal price = currencyService.getCurrencyPrice(usd.getSymbol());
        //then
        assertEquals(usd.getPrice(), price);
        verify(currencyRepository, times(1)).findBySymbol(usd.getSymbol());
    }

    @Test
    void shouldUpdateCurrencies() {
        //given
        Currency updatedBtc = Currency.builder()
                .symbol(usd.getSymbol())
                .price(new BigDecimal("60000"))
                .build();
        when(currencyapiClient.fetchCurrencies()).thenReturn(List.of(updatedBtc));
        when(currencyRepository.findBySymbol(usd.getSymbol())).thenReturn(Optional.of(usd));
        //when
        currencyService.updateCurrencies();
        //then
        verify(currencyRepository, times(1)).save(usd);
        assertEquals(new BigDecimal("60000"), usd.getPrice());
    }

    @Test
    void shouldGetAllCurrenciesByUserId() throws UserException {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(currencyRepository.findAll()).thenReturn(List.of(usd, pln));
        //when
        List<Currency> result = currencyService.getAllCurrenciesByUserId(1L);
        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(usd.getSymbol(), result.getFirst().getSymbol());
        assertEquals(usd.getPrice().divide(mainCurrency.getPrice(), 2, RoundingMode.CEILING), result.getFirst().getPrice());
        assertEquals(pln.getSymbol(), result.getLast().getSymbol());
        assertEquals(pln.getPrice().divide(mainCurrency.getPrice(), 2, RoundingMode.CEILING), result.getLast().getPrice());
        verify(userRepository, times(1)).findById(1L);
        verify(currencyRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForCurrencies() {
        //given
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        //when & then
        assertThrows(UserException.class, () -> currencyService.getAllCurrenciesByUserId(2L));
        verify(userRepository, times(1)).findById(2L);
    }
}
package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CurrencyDto;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.CurrencyMapper;
import com.konrad.smartfinance.service.CurrencyService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringJUnitWebConfig
@WebMvcTest(CurrencyController.class)
class CurrencyControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    @MockitoBean
    private CurrencyMapper currencyMapper;

    @Test
    @WithMockUser
    void shouldReturnEmptyListOnGetAllCurrencies() throws Exception {
        //given
        when(currencyService.getAllCurrencies()).thenReturn(Collections.emptyList());
        when(currencyMapper.mapToCurrencyDtoList(any())).thenReturn(Collections.emptyList());
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currencies").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        verify(currencyService, times(1)).getAllCurrencies();
    }

    @Test
    @WithMockUser
    void shouldGetAllCurrencies() throws Exception {
        //given
        Currency currency1 = Currency.builder()
                .id(1L)
                .symbol("USD")
                .build();
        Currency currency2 = Currency.builder()
                .id(2L)
                .symbol("EUR")
                .build();
        CurrencyDto currencyDto1 = CurrencyDto.builder()
                .id(1L)
                .symbol("USD")
                .build();
        CurrencyDto currencyDto2 = CurrencyDto.builder()
                .symbol("EUR")
                .build();
        List<Currency> currencies = List.of(currency1, currency2);
        List<CurrencyDto> currencyDtos = List.of(currencyDto1, currencyDto2);
        when(currencyService.getAllCurrencies()).thenReturn(currencies);
        when(currencyMapper.mapToCurrencyDtoList(currencies)).thenReturn(currencyDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currencies").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
        verify(currencyService, times(1)).getAllCurrencies();
        verify(currencyMapper, times(1)).mapToCurrencyDtoList(currencies);
    }

    @Test
    @WithMockUser
    void shouldGetAllCurrenciesByUserId() throws Exception {
        //given
        Long userId = 1L;
        Currency currency1 = Currency.builder()
                .id(1L)
                .symbol("btc")
                .build();
        CurrencyDto currencyDto1 = CurrencyDto.builder()
                .id(1L)
                .symbol("btc")
                .build();
        List<Currency> currencies = List.of(currency1);
        List<CurrencyDto> currencyDtos = List.of(currencyDto1);
        when(currencyMapper.mapToCurrencyDtoList(currencies)).thenReturn(currencyDtos);
        when(currencyService.getAllCurrenciesByUserId(userId)).thenReturn(currencies);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currencies/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol", Matchers.is("btc")));
        verify(currencyService, times(1)).getAllCurrenciesByUserId(userId);
        verify(currencyMapper, times(1)).mapToCurrencyDtoList(currencies);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetAllCurrenciesByUserId() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(currencyService).getAllCurrenciesByUserId(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currencies/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(currencyService, times(1)).getAllCurrenciesByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldGetCurrencyBySymbol() throws Exception {
        //given
        String symbol = "btc";
        Currency currency = Currency.builder()
                .id(1L)
                .symbol("btc")
                .build();
        CurrencyDto currencyDto = CurrencyDto.builder()
                .id(1L)
                .symbol("btc")
                .build();
        when(currencyMapper.mapToCurrencyDto(currency)).thenReturn(currencyDto);
        when(currencyService.getCurrencyBySymbol(symbol)).thenReturn(currency);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currencies/{symbol}", symbol).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.symbol", Matchers.is("btc")));
        verify(currencyService, times(1)).getCurrencyBySymbol(symbol);
        verify(currencyMapper, times(1)).mapToCurrencyDto(currency);
    }

    @Test
    @WithMockUser
    void shouldGetPriceBySymbol() throws Exception {
        //given
        String symbol = "btc";
        when(currencyService.getCurrencyPrice(symbol)).thenReturn(BigDecimal.TEN);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currencies/{symbol}/price", symbol).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(BigDecimal.TEN.toString())));
        verify(currencyService, times(1)).getCurrencyPrice(symbol);
    }

}
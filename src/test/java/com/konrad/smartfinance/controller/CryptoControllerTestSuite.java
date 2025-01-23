package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CryptocurrencyDto;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.CryptocurrencyMapper;
import com.konrad.smartfinance.service.CryptocurrencyService;
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
@WebMvcTest(CryptoController.class)
class CryptoControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CryptocurrencyService cryptocurrencyService;

    @MockitoBean
    private CryptocurrencyMapper cryptocurrencyMapper;

    @Test
    @WithMockUser
    void shouldReturnEmptyListOnGetAllCryptocurrencies() throws Exception {
        //given
        when(cryptocurrencyService.getAll()).thenReturn(Collections.emptyList());
        when(cryptocurrencyMapper.mapToCryptocurrencyDtoList(any())).thenReturn(Collections.emptyList());
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        verify(cryptocurrencyService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    void shouldGetAllCryptocurrencies() throws Exception {
        //given
        Cryptocurrency cryptocurrency1 = Cryptocurrency.builder()
                .id(1L)
                .symbol("btc")
                .build();
        Cryptocurrency cryptocurrency2 = Cryptocurrency.builder()
                .id(2L)
                .symbol("eth")
                .build();
        CryptocurrencyDto cryptocurrencyDto1 = CryptocurrencyDto.builder()
                .id(1L)
                .symbol("btc")
                .build();
        CryptocurrencyDto cryptocurrencyDto2 = CryptocurrencyDto.builder()
                .symbol("eth")
                .build();
        List<Cryptocurrency> cryptocurrencies = List.of(cryptocurrency1, cryptocurrency2);
        List<CryptocurrencyDto> cryptocurrencyDtos = List.of(cryptocurrencyDto1, cryptocurrencyDto2);
        when(cryptocurrencyService.getAll()).thenReturn(cryptocurrencies);
        when(cryptocurrencyMapper.mapToCryptocurrencyDtoList(cryptocurrencies)).thenReturn(cryptocurrencyDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
        verify(cryptocurrencyService, times(1)).getAll();
        verify(cryptocurrencyMapper, times(1)).mapToCryptocurrencyDtoList(cryptocurrencies);
    }

    @Test
    @WithMockUser
    void shouldGetAllCryptocurrenciesByUserId() throws Exception {
        //given
        Long userId = 1L;
        Cryptocurrency cryptocurrency1 = Cryptocurrency.builder()
                .id(1L)
                .symbol("btc")
                .build();
        CryptocurrencyDto cryptocurrencyDto1 = CryptocurrencyDto.builder()
                .id(1L)
                .symbol("btc")
                .build();
        List<Cryptocurrency> cryptocurrencies = List.of(cryptocurrency1);
        List<CryptocurrencyDto> cryptocurrencyDtos = List.of(cryptocurrencyDto1);
        when(cryptocurrencyMapper.mapToCryptocurrencyDtoList(cryptocurrencies)).thenReturn(cryptocurrencyDtos);
        when(cryptocurrencyService.getAllCryptocurrenciesByUserId(userId)).thenReturn(cryptocurrencies);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol", Matchers.is("btc")));
        verify(cryptocurrencyService, times(1)).getAllCryptocurrenciesByUserId(userId);
        verify(cryptocurrencyMapper, times(1)).mapToCryptocurrencyDtoList(cryptocurrencies);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetAllCryptocurrenciesByUserId() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(cryptocurrencyService).getAllCryptocurrenciesByUserId(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(cryptocurrencyService, times(1)).getAllCryptocurrenciesByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldGetCryptocurrencyBySymbol() throws Exception {
        //given
        String symbol = "btc";
        Cryptocurrency cryptocurrency = Cryptocurrency.builder()
                .id(1L)
                .symbol("btc")
                .build();
        CryptocurrencyDto cryptocurrencyDto = CryptocurrencyDto.builder()
                .id(1L)
                .symbol("btc")
                .build();
        when(cryptocurrencyMapper.mapToCryptocurrencyDto(cryptocurrency)).thenReturn(cryptocurrencyDto);
        when(cryptocurrencyService.getBySymbol(symbol)).thenReturn(cryptocurrency);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto/{symbol}", symbol).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.symbol", Matchers.is("btc")));
        verify(cryptocurrencyService, times(1)).getBySymbol(symbol);
        verify(cryptocurrencyMapper, times(1)).mapToCryptocurrencyDto(cryptocurrency);
    }

    @Test
    @WithMockUser
    void shouldGetPriceBySymbol() throws Exception {
        //given
        String symbol = "btc";
        when(cryptocurrencyService.getPrice(symbol)).thenReturn(BigDecimal.TEN);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto/{symbol}/price", symbol).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(BigDecimal.TEN.toString())));
        verify(cryptocurrencyService, times(1)).getPrice(symbol);
    }
}
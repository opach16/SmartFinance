package com.konrad.smartfinance.controller;

import com.google.gson.*;
import com.konrad.smartfinance.domain.CurrencyTransactionType;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.*;
import com.konrad.smartfinance.mapper.CurrencyTransactionMapper;
import com.konrad.smartfinance.service.CurrencyTransactionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringJUnitWebConfig
@WebMvcTest(CurrencyTransactionController.class)
class CurrencyTransactionControllerTestSuite {


    private User user;
    private Currency currency;
    private CurrencyTransaction currencyTransaction;
    private CurrencyTransactionRequest request;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyTransactionService currencyTransactionService;

    @MockitoBean
    private CurrencyTransactionMapper currencyTransactionMapper;

    private final GsonBuilder gsonBuilder =
            new GsonBuilder().registerTypeAdapter(LocalDate.class, new CurrencyTransactionControllerTestSuite.LocalDateSerializer());

    private final Gson gson = gsonBuilder.create();

    @BeforeEach
    void setUp() {
        currency = Currency.builder().id(1L).build();
        user = User.builder().id(1L).build();
        currencyTransaction = CurrencyTransaction.builder()
                .id(1L)
                .currency(currency)
                .user(user)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .amount(BigDecimal.ONE)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 0))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 0))
                .price(BigDecimal.TEN)
                .build();
        request = CurrencyTransactionRequest.builder()
                .id(1L)
                .userId(1L)
                .transactionType(CurrencyTransactionType.BUY)
                .currency("USD")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListOnGetAllTransactions() throws Exception {
        //given
        when(currencyTransactionService.getAllTransactions()).thenReturn(Collections.emptyList());
        when(currencyTransactionMapper.mapToCurrencyTransactionDtoList(any())).thenReturn(Collections.emptyList());
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency-transactions").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        verify(currencyTransactionService, times(1)).getAllTransactions();
        verify(currencyTransactionMapper, times(1)).mapToCurrencyTransactionDtoList(any());
    }

    @Test
    @WithMockUser
    void shouldGetAllTransactions() throws Exception {
        //given
        CurrencyTransaction currencyTransaction2 = CurrencyTransaction.builder()
                .id(2L)
                .build();
        CurrencyTransactionDto currencyTransactionDto = CurrencyTransactionDto.builder()
                .id(1L)
                .build();
        CurrencyTransactionDto currencyTransactionDto2 = CurrencyTransactionDto.builder()
                .id(2L)
                .build();
        List<CurrencyTransaction> currencyTransactions = List.of(currencyTransaction, currencyTransaction2);
        List<CurrencyTransactionDto> currencyTransactionDtos = List.of(currencyTransactionDto, currencyTransactionDto2);
        when(currencyTransactionService.getAllTransactions()).thenReturn(currencyTransactions);
        when(currencyTransactionMapper.mapToCurrencyTransactionDtoList(currencyTransactions)).thenReturn(currencyTransactionDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency-transactions").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
        verify(currencyTransactionService, times(1)).getAllTransactions();
        verify(currencyTransactionMapper, times(1)).mapToCurrencyTransactionDtoList(currencyTransactions);
    }

    @Test
    @WithMockUser
    void shouldGetTransactionById() throws Exception {
        //given
        Long currencyTransactionId = 1L;
        CurrencyTransaction currencyTransaction = CurrencyTransaction.builder()
                .id(1L)
                .build();
        CurrencyTransactionDto currencyTransactionDto = CurrencyTransactionDto.builder()
                .id(1L)
                .build();
        when(currencyTransactionService.getTransactionById(currencyTransactionId)).thenReturn(currencyTransaction);
        when(currencyTransactionMapper.mapToCurrencyTransactionDto(any())).thenReturn(currencyTransactionDto);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency-transactions/{id}", currencyTransactionId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
        verify(currencyTransactionService, times(1)).getTransactionById(currencyTransactionId);
        verify(currencyTransactionMapper, times(1)).mapToCurrencyTransactionDto(currencyTransaction);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenTransactionNotFountOnGetTransactionById() throws Exception {
        //given
        Long currencyTransactionId = 1L;
        doThrow(new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND))
                .when(currencyTransactionService).getTransactionById(currencyTransactionId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency-transactions/{id}", currencyTransactionId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CurrencyTransactionException.NOT_FOUND)));
        verify(currencyTransactionService, times(1)).getTransactionById(currencyTransactionId);
    }

    @Test
    @WithMockUser
    void shouldGetTransactionsByUserId() throws Exception {
        //given
        Long userId = 1L;
        CurrencyTransactionDto currencyTransactionDto = CurrencyTransactionDto.builder()
                .id(1L)
                .build();
        List<CurrencyTransaction> currencyTransactions = List.of(currencyTransaction);
        List<CurrencyTransactionDto> currencyTransactionDtos = List.of(currencyTransactionDto);
        when(currencyTransactionService.getTransactionByUserId(userId)).thenReturn(currencyTransactions);
        when(currencyTransactionMapper.mapToCurrencyTransactionDtoList(currencyTransactions)).thenReturn(currencyTransactionDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency-transactions/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)));
        verify(currencyTransactionService, times(1)).getTransactionByUserId(userId);
        verify(currencyTransactionMapper, times(1)).mapToCurrencyTransactionDtoList(currencyTransactions);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetTransactionByUserId() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(currencyTransactionService).getTransactionByUserId(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency-transactions/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(currencyTransactionService, times(1)).getTransactionByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldAddTransaction() throws Exception {
        //given
        when(currencyTransactionService.addTransaction(any())).thenReturn(currencyTransaction);
        when(currencyTransactionMapper.mapToCurrencyTransactionRequest(currencyTransaction)).thenReturn(request);
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(10)));
        verify(currencyTransactionService, times(1)).addTransaction(any());
        verify(currencyTransactionMapper, times(1)).mapToCurrencyTransactionRequest(currencyTransaction);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(currencyTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(currencyTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAccountNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new AccountException(AccountException.NOT_FOUND)).when(currencyTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AccountException.NOT_FOUND)));
        verify(currencyTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAssetNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new AssetException(AssetException.NOT_FOUND)).when(currencyTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AssetException.NOT_FOUND)));
        verify(currencyTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCurrencyNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND)).when(currencyTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CurrencyExeption.CURRENCY_NOT_FOUND)));
        verify(currencyTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldUpdateTransaction() throws Exception {
        //given
        when(currencyTransactionService.updateTransaction(any())).thenReturn(currencyTransaction);
        when(currencyTransactionMapper.mapToCurrencyTransactionRequest(currencyTransaction)).thenReturn(request);
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(10)));
        verify(currencyTransactionService, times(1)).updateTransaction(any());
        verify(currencyTransactionMapper, times(1)).mapToCurrencyTransactionRequest(currencyTransaction);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(currencyTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(currencyTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAccountNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new AccountException(AccountException.NOT_FOUND)).when(currencyTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AccountException.NOT_FOUND)));
        verify(currencyTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAssetNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new AssetException(AssetException.NOT_FOUND)).when(currencyTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AssetException.NOT_FOUND)));
        verify(currencyTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCurrencyNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND)).when(currencyTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CurrencyExeption.CURRENCY_NOT_FOUND)));
        verify(currencyTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenTransactionNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new CurrencyTransactionException(CurrencyTransactionException.NOT_FOUND)).when(currencyTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/currency-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CurrencyTransactionException.NOT_FOUND)));
        verify(currencyTransactionService, times(1)).updateTransaction(any());
    }


    @Test
    @WithMockUser
    void shouldDeleteTransaction() throws Exception {
        //given
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/currency-transactions").with(csrf())
                        .queryParam("transactionId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(currencyTransactionService, times(1)).deleteTransaction(1L);
    }

    private static final class LocalDateSerializer implements JsonSerializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDate));
        }
    }

}
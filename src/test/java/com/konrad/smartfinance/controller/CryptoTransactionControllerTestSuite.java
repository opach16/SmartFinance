package com.konrad.smartfinance.controller;

import com.google.gson.*;
import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.CryptoTransaction;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.*;
import com.konrad.smartfinance.mapper.CryptoTransactionMapper;
import com.konrad.smartfinance.service.CryptoTransactionService;
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
@WebMvcTest(CryptoTransactionController.class)
class CryptoTransactionControllerTestSuite {

    private User user;
    private Cryptocurrency crypto;
    private CryptoTransaction cryptoTransaction;
    private CryptoTransactionRequest request;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CryptoTransactionService cryptoTransactionService;

    @MockitoBean
    private CryptoTransactionMapper cryptoTransactionMapper;

    private final GsonBuilder gsonBuilder =
            new GsonBuilder().registerTypeAdapter(LocalDate.class, new CryptoTransactionControllerTestSuite.LocalDateSerializer());

    private final Gson gson = gsonBuilder.create();

    @BeforeEach
    void setUp() {
        crypto = Cryptocurrency.builder().id(1L).build();
        user = User.builder().id(1L).build();
        cryptoTransaction = CryptoTransaction.builder()
                .id(1L)
                .cryptocurrency(crypto)
                .user(user)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .amount(BigDecimal.ONE)
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 0))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 0))
                .price(BigDecimal.TEN)
                .build();
        request = CryptoTransactionRequest.builder()
                .id(1L)
                .userId(1L)
                .transactionType(CryptoTransactionType.BUY)
                .cryptocurrency("btc")
                .name("bitcoin")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListOnGetAllCryptoTransactions() throws Exception {
        //given
        when(cryptoTransactionService.getAllTransactions()).thenReturn(Collections.emptyList());
        when(cryptoTransactionMapper.mapToCryptoTransactionDtoList(any())).thenReturn(Collections.emptyList());
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto-transactions").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        verify(cryptoTransactionService, times(1)).getAllTransactions();
        verify(cryptoTransactionMapper, times(1)).mapToCryptoTransactionDtoList(any());
    }

    @Test
    @WithMockUser
    void shouldGetAllTransactions() throws Exception {
        //given
        CryptoTransaction cryptoTransaction2 = CryptoTransaction.builder()
                .id(2L)
                .build();
        CryptoTransactionDto cryptoTransactionDto = CryptoTransactionDto.builder()
                .id(1L)
                .build();
        CryptoTransactionDto cryptoTransactionDto2 = CryptoTransactionDto.builder()
                .id(2L)
                .build();
        List<CryptoTransaction> cryptoTransactions = List.of(cryptoTransaction, cryptoTransaction2);
        List<CryptoTransactionDto> cryptoTransactionDtos = List.of(cryptoTransactionDto, cryptoTransactionDto2);
        when(cryptoTransactionService.getAllTransactions()).thenReturn(cryptoTransactions);
        when(cryptoTransactionMapper.mapToCryptoTransactionDtoList(cryptoTransactions)).thenReturn(cryptoTransactionDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto-transactions").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
        verify(cryptoTransactionService, times(1)).getAllTransactions();
        verify(cryptoTransactionMapper, times(1)).mapToCryptoTransactionDtoList(cryptoTransactions);
    }

    @Test
    @WithMockUser
    void shouldGetTransactionById() throws Exception {
        //given
        Long cryptoTransactionId = 1L;
        CryptoTransaction cryptoTransaction = CryptoTransaction.builder()
                .id(1L)
                .build();
        CryptoTransactionDto cryptoTransactionDto = CryptoTransactionDto.builder()
                .id(1L)
                .build();
        when(cryptoTransactionService.getTransactionById(cryptoTransactionId)).thenReturn(cryptoTransaction);
        when(cryptoTransactionMapper.mapToCryptoTransactionDto(any())).thenReturn(cryptoTransactionDto);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto-transactions/{id}", cryptoTransactionId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
        verify(cryptoTransactionService, times(1)).getTransactionById(cryptoTransactionId);
        verify(cryptoTransactionMapper, times(1)).mapToCryptoTransactionDto(cryptoTransaction);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCryptoTransactionNotFountOnGetTransactionById() throws Exception {
        //given
        Long cryptoTransactionId = 1L;
        doThrow(new CryptoTransactionException(CryptoTransactionException.NOT_FOUND))
                .when(cryptoTransactionService).getTransactionById(cryptoTransactionId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto-transactions/{id}", cryptoTransactionId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CryptoTransactionException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).getTransactionById(cryptoTransactionId);
    }

    @Test
    @WithMockUser
    void shouldGetTransactionsByUserId() throws Exception {
        //given
        Long userId = 1L;
        CryptoTransactionDto cryptoTransactionDto = CryptoTransactionDto.builder()
                .id(1L)
                .build();
        List<CryptoTransaction> cryptoTransactions = List.of(cryptoTransaction);
        List<CryptoTransactionDto> cryptoTransactionDtos = List.of(cryptoTransactionDto);
        when(cryptoTransactionService.getTransactionsByUserId(userId)).thenReturn(cryptoTransactions);
        when(cryptoTransactionMapper.mapToCryptoTransactionDtoList(cryptoTransactions)).thenReturn(cryptoTransactionDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto-transactions/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)));
        verify(cryptoTransactionService, times(1)).getTransactionsByUserId(userId);
        verify(cryptoTransactionMapper, times(1)).mapToCryptoTransactionDtoList(cryptoTransactions);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetTransactionByUserId() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(cryptoTransactionService).getTransactionsByUserId(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/crypto-transactions/user/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).getTransactionsByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldAddTransaction() throws Exception {
        //given
        when(cryptoTransactionService.addTransaction(any())).thenReturn(cryptoTransaction);
        when(cryptoTransactionMapper.mapToCryptoTransactionRequest(cryptoTransaction)).thenReturn(request);
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(10)));
        verify(cryptoTransactionService, times(1)).addTransaction(any());
        verify(cryptoTransactionMapper, times(1)).mapToCryptoTransactionRequest(cryptoTransaction);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(cryptoTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAccountNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new AccountException(AccountException.NOT_FOUND)).when(cryptoTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AccountException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAssetNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new AssetException(AssetException.NOT_FOUND)).when(cryptoTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AssetException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCryptocurrencyNotFoundOnAddTransaction() throws Exception {
        //given
        doThrow(new CryptocurrencyException(CryptocurrencyException.NOT_FOUND)).when(cryptoTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CryptocurrencyException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldUpdateTransaction() throws Exception {
        //given
        when(cryptoTransactionService.updateTransaction(any())).thenReturn(cryptoTransaction);
        when(cryptoTransactionMapper.mapToCryptoTransactionRequest(cryptoTransaction)).thenReturn(request);
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(10)));
        verify(cryptoTransactionService, times(1)).updateTransaction(any());
        verify(cryptoTransactionMapper, times(1)).mapToCryptoTransactionRequest(cryptoTransaction);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(cryptoTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAccountNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new AccountException(AccountException.NOT_FOUND)).when(cryptoTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AccountException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAssetNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new AssetException(AssetException.NOT_FOUND)).when(cryptoTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AssetException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCryptocurrencyNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new CryptocurrencyException(CryptocurrencyException.NOT_FOUND)).when(cryptoTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CryptocurrencyException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenTransactionNotFoundOnUpdateTransaction() throws Exception {
        //given
        doThrow(new CryptoTransactionException(CryptoTransactionException.NOT_FOUND)).when(cryptoTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/crypto-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CryptoTransactionException.NOT_FOUND)));
        verify(cryptoTransactionService, times(1)).updateTransaction(any());
    }


    @Test
    @WithMockUser
    void shouldDeleteTransaction() throws Exception {
        //given
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/crypto-transactions").with(csrf())
                        .queryParam("transactionId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(cryptoTransactionService, times(1)).deleteTransaction(1L);
    }

    private static final class LocalDateSerializer implements JsonSerializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDate));
        }
    }
}
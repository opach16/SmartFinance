package com.konrad.smartfinance.controller;

import com.google.gson.*;
import com.konrad.smartfinance.domain.DebitTransactionType;
import com.konrad.smartfinance.domain.dto.DebitTransactionDto;
import com.konrad.smartfinance.domain.dto.DebitTransactionRequest;
import com.konrad.smartfinance.domain.model.DebitTransaction;
import com.konrad.smartfinance.exception.AccountException;
import com.konrad.smartfinance.exception.DebitTransactionException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.DebitTransactionMapper;
import com.konrad.smartfinance.service.DebitTransactionService;
import org.hamcrest.Matchers;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringJUnitWebConfig
@WebMvcTest(DebitTransactionController.class)
class DebitTransactionControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DebitTransactionService debitTransactionService;

    @MockitoBean
    private DebitTransactionMapper debitTransactionMapper;

    private final GsonBuilder gsonBuilder =
            new GsonBuilder().registerTypeAdapter(LocalDate.class, new DebitTransactionControllerTestSuite.LocalDateSerializer());

    private final Gson gson = gsonBuilder.create();

    @Test
    @WithMockUser
    void shouldGetTransactionById() throws Exception {
        //given
        Long transactionId = 1L;
        DebitTransaction debitTransaction = DebitTransaction.builder()
                .id(1L)
                .build();
        DebitTransactionDto debitTransactionDto = DebitTransactionDto.builder()
                .id(1L)
                .build();
        when(debitTransactionService.getTransactionById(transactionId)).thenReturn(debitTransaction);
        when(debitTransactionMapper.mapToDebitTransactionDto(debitTransaction)).thenReturn(debitTransactionDto);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}", transactionId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(debitTransactionDto.getId()));
        verify(debitTransactionService).getTransactionById(transactionId);
        verify(debitTransactionMapper).mapToDebitTransactionDto(debitTransaction);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestOnGetTransactionById() throws Exception {
        //given
        Long transactionId = 1L;
        doThrow(new DebitTransactionException(DebitTransactionException.NOT_FOUND)).when(debitTransactionService).getTransactionById(transactionId);
        //when
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}", transactionId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(DebitTransactionException.NOT_FOUND)));
        verify(debitTransactionService).getTransactionById(transactionId);
    }

    @Test
    @WithMockUser
    void shouldGetAllTransactions() throws Exception {
        //given
        Long userId = 1L;
        DebitTransaction debitTransaction1 = DebitTransaction.builder()
                .id(1L)
                .build();
        DebitTransaction debitTransaction2 = DebitTransaction.builder()
                .id(2L)
                .build();
        DebitTransactionDto debitTransactionDto1 = DebitTransactionDto.builder()
                .id(1L)
                .build();
        DebitTransactionDto debitTransactionDto2 = DebitTransactionDto.builder()
                .id(2L)
                .build();
        List<DebitTransaction> debitTransactions = List.of(debitTransaction1, debitTransaction2);
        List<DebitTransactionDto> debitTransactionDtos = List.of(debitTransactionDto1, debitTransactionDto2);
        when(debitTransactionService.getAllTransactions(userId)).thenReturn(debitTransactions);
        when(debitTransactionMapper.mapToDebitTransactionDtoList(debitTransactions)).thenReturn(debitTransactionDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}/all", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
        verify(debitTransactionService).getAllTransactions(userId);
        verify(debitTransactionMapper).mapToDebitTransactionDtoList(debitTransactions);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetAllTransactions() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(debitTransactionService).getAllTransactions(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}/all", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(debitTransactionService).getAllTransactions(userId);
    }

    @Test
    @WithMockUser
    void shouldGetAllExpenses() throws Exception {
        //given
        Long userId = 1L;
        DebitTransactionDto debitTransactionDto1 = DebitTransactionDto.builder()
                .id(1L)
                .transactionType(DebitTransactionType.EXPENSE)
                .build();
        DebitTransactionDto debitTransactionDto2 = DebitTransactionDto.builder()
                .id(2L)
                .transactionType(DebitTransactionType.EXPENSE)
                .build();
        List<DebitTransactionDto> debitTransactionDtos = List.of(debitTransactionDto1, debitTransactionDto2);
        when(debitTransactionService.getAllExpenses(any())).thenReturn(null);
        when(debitTransactionMapper.mapToDebitTransactionDtoList(any())).thenReturn(debitTransactionDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}/expenses", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionType", Matchers.is(DebitTransactionType.EXPENSE.toString())));
        verify(debitTransactionService).getAllExpenses(any());
        verify(debitTransactionMapper).mapToDebitTransactionDtoList(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetAllExpenses() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(debitTransactionService).getAllExpenses(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}/expenses", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(debitTransactionService).getAllExpenses(userId);
    }

    @Test
    @WithMockUser
    void shouldGetAllIncomes() throws Exception {
        //given
        Long userId = 1L;
        DebitTransactionDto debitTransactionDto1 = DebitTransactionDto.builder()
                .id(1L)
                .transactionType(DebitTransactionType.INCOME)
                .build();
        DebitTransactionDto debitTransactionDto2 = DebitTransactionDto.builder()
                .id(2L)
                .transactionType(DebitTransactionType.INCOME)
                .build();
        List<DebitTransactionDto> debitTransactionDtos = List.of(debitTransactionDto1, debitTransactionDto2);
        when(debitTransactionService.getAllIncomes(any())).thenReturn(null);
        when(debitTransactionMapper.mapToDebitTransactionDtoList(any())).thenReturn(debitTransactionDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}/incomes", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionType", Matchers.is(DebitTransactionType.INCOME.toString())));
        verify(debitTransactionService).getAllIncomes(any());
        verify(debitTransactionMapper).mapToDebitTransactionDtoList(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetAllIncomes() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(debitTransactionService).getAllIncomes(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/debit-transactions/{id}/incomes", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(debitTransactionService).getAllIncomes(userId);
    }

    @Test
    @WithMockUser
    void shouldAddTransaction() throws Exception {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(1L)
                .transactionId(1L)
                .transactionType(DebitTransactionType.EXPENSE)
                .currency("USD")
                .name("testName")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        String requestJson = gson.toJson(request);
        when(debitTransactionService.addTransaction(any())).thenReturn(null);
        when(debitTransactionMapper.mapToDebitTransactionRequest(any())).thenReturn(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/debit-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType", Matchers.is("EXPENSE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("USD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("testName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionDate", Matchers.is("2020-01-01")));
        verify(debitTransactionService).addTransaction(any());
        verify(debitTransactionMapper).mapToDebitTransactionRequest(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnAddTransaction() throws Exception {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(1L)
                .transactionId(1L)
                .transactionType(DebitTransactionType.EXPENSE)
                .currency("USD")
                .name("testName")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(debitTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/debit-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(debitTransactionService).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAccountNotFoundOnAddTransaction() throws Exception {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(1L)
                .transactionId(1L)
                .transactionType(DebitTransactionType.EXPENSE)
                .currency("USD")
                .name("testName")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        doThrow(new AccountException(AccountException.NOT_FOUND)).when(debitTransactionService).addTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/debit-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AccountException.NOT_FOUND)));
        verify(debitTransactionService).addTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldUpdateTransaction() throws Exception {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(2L)
                .transactionId(2L)
                .transactionType(DebitTransactionType.INCOME)
                .currency("updatedUSD")
                .name("updatedTestName")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        String requestJson = gson.toJson(request);
        when(debitTransactionService.updateTransaction(any())).thenReturn(null);
        when(debitTransactionMapper.mapToDebitTransactionRequest(any())).thenReturn(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/debit-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType", Matchers.is("INCOME")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("updatedUSD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("updatedTestName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionDate", Matchers.is("2020-01-01")));
        verify(debitTransactionService).updateTransaction(any());
        verify(debitTransactionMapper).mapToDebitTransactionRequest(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenAccountNotFoundOnUpdateTransaction() throws Exception {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(2L)
                .transactionId(2L)
                .transactionType(DebitTransactionType.INCOME)
                .currency("updatedUSD")
                .name("updatedTestName")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        doThrow(new AccountException(AccountException.NOT_FOUND)).when(debitTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/debit-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(AccountException.NOT_FOUND)));
        verify(debitTransactionService).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenTransactionNotFoundOnUpdateTransaction() throws Exception {
        //given
        DebitTransactionRequest request = DebitTransactionRequest.builder()
                .userId(2L)
                .transactionId(2L)
                .transactionType(DebitTransactionType.INCOME)
                .currency("updatedUSD")
                .name("updatedTestName")
                .amount(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .build();
        doThrow(new DebitTransactionException(DebitTransactionException.NOT_FOUND)).when(debitTransactionService).updateTransaction(any());
        String requestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/debit-transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(DebitTransactionException.NOT_FOUND)));
        verify(debitTransactionService).updateTransaction(any());
    }

    @Test
    @WithMockUser
    void shouldDeleteTransaction() throws Exception {
        //given
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/debit-transactions").with(csrf())
                        .queryParam("transactionId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(debitTransactionService).deleteTransaction(1L);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenTransactionNotFoundOnDeleteTransaction() throws Exception {
        //given
        Long transactionId = 1L;
        doThrow(new DebitTransactionException(DebitTransactionException.NOT_FOUND))
                .when(debitTransactionService).deleteTransaction(transactionId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/debit-transactions").with(csrf())
                        .queryParam("transactionId", "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(DebitTransactionException.NOT_FOUND)));
        verify(debitTransactionService).deleteTransaction(transactionId);
    }

    private static final class LocalDateSerializer implements JsonSerializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDate));
        }
    }
}
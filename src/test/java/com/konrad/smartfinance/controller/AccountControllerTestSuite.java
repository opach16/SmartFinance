package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.AccountMapper;
import com.konrad.smartfinance.service.AccountService;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringJUnitWebConfig
@WebMvcTest(AccountController.class)
class AccountControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AccountMapper accountMapper;


    @Test
    @WithMockUser
    void shouldGetAccount() throws Exception {
        //given
        Long userId = 1L;
        Account account = Account.builder()
                .id(1L)
                .build();
        AccountDto accountDto = new AccountDto.Builder()
                .id(1L)
                .build();
        when(accountMapper.mapToAccountDto(account)).thenReturn(accountDto);
        when(accountService.getAccountByUserId(userId)).thenReturn(account);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
        verify(accountMapper, times(1)).mapToAccountDto(account);
        verify(accountService, times(1)).getAccountByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetAccount() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(accountService).getAccountByUserId(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(accountService, times(1)).getAccountByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldGetMainBalance() throws Exception {
        //given
        Long userId = 1L;
        when(accountService.getMainBalanceByUserId(userId)).thenReturn(BigDecimal.ONE);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/main-balance/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(BigDecimal.ONE.toString())));
        verify(accountService, times(1)).getMainBalanceByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldGetAssetsBalance() throws Exception {
        //given
        Long userId = 1L;
        when(accountService.getAssetsBalanceByUserId(userId)).thenReturn(BigDecimal.ONE);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/assets-balance/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(BigDecimal.ONE.toString())));
        verify(accountService, times(1)).getAssetsBalanceByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldGetTotalBalance() throws Exception {
        //given
        Long userId = 1L;
        when(accountService.getTotalBalanceByUserId(userId)).thenReturn(BigDecimal.ONE);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/total-balance/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(BigDecimal.ONE.toString())));
        verify(accountService, times(1)).getTotalBalanceByUserId(userId);
    }
}
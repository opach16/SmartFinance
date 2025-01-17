package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.DebitTransactionType;
import com.konrad.smartfinance.domain.dto.DebitTransactionDto;
import com.konrad.smartfinance.domain.dto.DebitTransactionRequest;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.DebitTransaction;
import com.konrad.smartfinance.domain.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class DebitTransactionMapperTestSuite {

    private User user;
    private Account account;
    private Currency currency;

    @BeforeEach
    void setUp() {
        currency = Currency.builder()
                .id(1L)
                .symbol("USD")
                .price(new BigDecimal("5"))
                .build();
        account = Account.builder()
                .id(1L)
                .user(user)
                .mainCurrency(currency)
                .mainBalance(new BigDecimal("100"))
                .build();
        user = User.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        user.setAccount(account);
    }

    @Autowired
    private DebitTransactionMapper debitTransactionMapper;
    @Test
    void shouldMapToDebitTransactionDto() {
        //given
        DebitTransaction debitTransaction = DebitTransaction.builder()
                .id(1L)
                .user(user)
                .transactionType(DebitTransactionType.EXPENSE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .name("testName")
                .amount(new BigDecimal("50"))
                .price(new BigDecimal("4"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 2, 2, 2, 2))
                .build();
        //when
        DebitTransactionDto debitTransactionDto = debitTransactionMapper.mapToDebitTransactionDto(debitTransaction);
        //then
        assertNotNull(debitTransactionDto);
        assertEquals(debitTransaction.getId(), debitTransactionDto.getId());
        assertEquals(debitTransaction.getTransactionType(), debitTransactionDto.getTransactionType());
        assertEquals(debitTransaction.getTransactionDate(), debitTransactionDto.getTransactionDate());
        assertEquals(debitTransaction.getName(), debitTransactionDto.getName());
        assertEquals(debitTransaction.getAmount(), debitTransactionDto.getAmount());
        assertEquals(debitTransaction.getCreatedAt(), debitTransactionDto.getCreatedAt());
        assertEquals(debitTransaction.getUpdatedAt(), debitTransactionDto.getUpdatedAt());
    }

    @Test
    void shouldMapToDebitTransactionDtoList() {
        //given
        DebitTransaction debitTransaction1 = DebitTransaction.builder()
                .id(1L)
                .user(user)
                .transactionType(DebitTransactionType.EXPENSE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .name("testName")
                .amount(new BigDecimal("50"))
                .price(new BigDecimal("4"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 2, 2, 2, 2))
                .build();
        DebitTransaction debitTransaction2 = DebitTransaction.builder()
                .id(2L)
                .user(user)
                .transactionType(DebitTransactionType.INCOME)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .name("testName1")
                .amount(new BigDecimal("100"))
                .price(new BigDecimal("3"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 2, 2, 2, 2))
                .build();
        List<DebitTransaction> debitTransactionList = List.of(debitTransaction1, debitTransaction2);
        //when
        List<DebitTransactionDto> debitTransactionDtoList = debitTransactionMapper.mapToDebitTransactionDtoList(debitTransactionList);
        //then
        assertNotNull(debitTransactionDtoList);
        assertEquals(debitTransactionList.size(), debitTransactionDtoList.size());
        assertEquals(debitTransaction1.getId(), debitTransactionDtoList.getFirst().getId());
        assertEquals(debitTransaction1.getUser().getId(), debitTransactionDtoList.getFirst().getUser().getId());
        assertEquals(debitTransaction2.getId(), debitTransactionDtoList.getLast().getId());
    }

    @Test
    void shouldMapToDebitTransactionRequest() {
        //given
        DebitTransaction debitTransaction = DebitTransaction.builder()
                .id(1L)
                .user(user)
                .transactionType(DebitTransactionType.EXPENSE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .name("testName")
                .amount(new BigDecimal("50"))
                .price(new BigDecimal("4"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 2, 2, 2, 2))
                .build();
        //when
        DebitTransactionRequest debitTransactionRequest = debitTransactionMapper.mapToDebitTransactionRequest(debitTransaction);
        //then
        assertNotNull(debitTransactionRequest);
        assertEquals(debitTransaction.getId(), debitTransactionRequest.getTransactionId());
        assertEquals(debitTransaction.getTransactionType(), debitTransactionRequest.getTransactionType());
        assertEquals(debitTransaction.getTransactionDate(), debitTransactionRequest.getTransactionDate());
        assertEquals(debitTransaction.getAmount(), debitTransactionRequest.getAmount());
        assertEquals(debitTransaction.getPrice(), debitTransactionRequest.getPrice());
        assertEquals(debitTransaction.getName(), debitTransactionRequest.getName());
        assertEquals(currency.getSymbol(), debitTransactionRequest.getCurrency());
    }
}